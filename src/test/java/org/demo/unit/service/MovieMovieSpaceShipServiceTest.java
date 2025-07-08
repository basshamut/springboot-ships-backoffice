package org.demo.unit.service;

import org.demo.dto.MovieSpaceShipsDto;
import org.demo.exception.ServiceException;
import org.demo.persistance.entities.MovieSpaceShip;
import org.demo.persistance.repository.MovieSpaceShipRepository;
import org.demo.service.MovieSpaceShipService;
import org.demo.service.mapper.SpaceShipMapper;
import org.demo.service.rabbitmq.RabbitMQSenderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.demo.dto.AuditEventDto;
import java.time.LocalDateTime;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovieMovieSpaceShipServiceTest {

    @Mock
    private MovieSpaceShipRepository movieSpaceShipRepository;

    @Mock
    private RabbitMQSenderService rabbitMQSenderService;

    @InjectMocks
    private MovieSpaceShipService movieSpaceShipService;

    private static final long TEST_ID = 1;
    private static final String TEST_NAME = "Test SpaceShip";
    private static final String TEST_MOVIE = "Test Movie";
    private static final MovieSpaceShip TEST_SPACE_SHIP_DTO = new MovieSpaceShip(TEST_ID, TEST_NAME, TEST_MOVIE);

    @BeforeEach
    public void setup() {
        reset(movieSpaceShipRepository);
        reset(rabbitMQSenderService);
        // Mockear el usuario autenticado para todos los tests
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken("usuarioTest", null)
        );
    }

    @Test
    public void testGetSpaceShips() {
        Page<MovieSpaceShip> page = new PageImpl<>(Collections.singletonList(TEST_SPACE_SHIP_DTO));
        when(movieSpaceShipRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(movieSpaceShipRepository.count()).thenReturn(1L);
        Page<MovieSpaceShipsDto> result = movieSpaceShipService.getSpaceShips(Pageable.unpaged());
        Assertions.assertEquals(1, result.getContent().size());
        Assertions.assertEquals(TEST_NAME, result.getContent().getFirst().getName());
        Assertions.assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testGetSpaceShipById() {
        when(movieSpaceShipRepository.findById(eq(TEST_ID))).thenReturn(Optional.of(TEST_SPACE_SHIP_DTO));
        MovieSpaceShipsDto result = movieSpaceShipService.getSpaceShipById(TEST_ID);
        Assertions.assertEquals(TEST_ID, result.getId());
        Assertions.assertEquals(TEST_NAME, result.getName());
    }

    @Test
    public void testGetSpaceShipById_NotFound() {
        when(movieSpaceShipRepository.findById(eq(TEST_ID))).thenReturn(Optional.empty());
        Assertions.assertThrows(ServiceException.class, () -> movieSpaceShipService.getSpaceShipById(TEST_ID));
    }

    @Test
    public void testGetSpaceShipByName() {
        when(movieSpaceShipRepository.findByNameContaining(eq(TEST_NAME))).thenReturn(Collections.singleton(TEST_SPACE_SHIP_DTO));

        MovieSpaceShipsDto result = movieSpaceShipService.getSpaceShipByName(TEST_NAME).iterator().next();

        Assertions.assertEquals(TEST_ID, result.getId());
        Assertions.assertEquals(TEST_NAME, result.getName());
    }

    @Test
    public void testSaveSpaceShip() {
        // Mock para que el save devuelva la entidad con ID generado
        when(movieSpaceShipRepository.save(any())).thenAnswer(invocation -> {
            MovieSpaceShip spaceShip = invocation.getArgument(0);
            spaceShip.setId(TEST_ID); // Simulamos que la BD asigna el ID
            return spaceShip;
        });

        movieSpaceShipService.saveSpaceShip((SpaceShipMapper.MAPPER.mapToDto(TEST_SPACE_SHIP_DTO)));

        verify(movieSpaceShipRepository, times(1)).save(any());
        verify(rabbitMQSenderService, times(1)).sendAuditMessage(
                org.mockito.ArgumentMatchers.argThat(auditEvent ->
                        auditEvent.getUser().equals("usuarioTest") &&
                                auditEvent.getShipId().equals(TEST_ID) &&
                                auditEvent.getShipName().equals(TEST_NAME) &&
                                auditEvent.getOperation().equals("CREATE") &&
                                auditEvent.getTimestamp() != null
                )
        );
    }

    @Test
    public void testDeleteSpaceShip() {
        when(movieSpaceShipRepository.findById(eq(TEST_ID))).thenReturn(Optional.of(TEST_SPACE_SHIP_DTO));

        movieSpaceShipService.deleteSpaceShip(TEST_ID);

        verify(movieSpaceShipRepository, times(1)).deleteById(eq(TEST_ID));
        verify(rabbitMQSenderService, times(1)).sendAuditMessage(
            org.mockito.ArgumentMatchers.argThat(auditEvent ->
                auditEvent.getUser().equals("usuarioTest") &&
                auditEvent.getShipId().equals(TEST_ID) &&
                auditEvent.getShipName().equals(TEST_NAME) &&
                auditEvent.getOperation().equals("DELETE") &&
                auditEvent.getTimestamp() != null
            )
        );
    }

    @Test
    public void testUpdateSpaceShip() {
        when(movieSpaceShipRepository.findById(eq(TEST_ID))).thenReturn(Optional.of(TEST_SPACE_SHIP_DTO));

        movieSpaceShipService.updateSpaceShip(SpaceShipMapper.MAPPER.mapToDto(TEST_SPACE_SHIP_DTO));

        verify(movieSpaceShipRepository, times(1)).save(any());
        verify(rabbitMQSenderService, times(1)).sendAuditMessage(
                org.mockito.ArgumentMatchers.argThat(auditEvent ->
                        auditEvent.getUser().equals("usuarioTest") &&
                                auditEvent.getShipId().equals(TEST_ID) &&
                                auditEvent.getShipName().equals(TEST_NAME) &&
                                auditEvent.getOperation().equals("UPDATE") &&
                                auditEvent.getTimestamp() != null
                )
        );
    }

    @Test
    public void testUpdateSpaceShip_NotFound() {
        when(movieSpaceShipRepository.findById(eq(TEST_ID))).thenReturn(Optional.empty());

        Assertions.assertThrows(ServiceException.class, () -> movieSpaceShipService.updateSpaceShip((SpaceShipMapper.MAPPER.mapToDto(TEST_SPACE_SHIP_DTO))));
    }
}
