package org.demo;

import org.demo.dto.SpaceShipDto;
import org.demo.exception.ServiceException;
import org.demo.repository.SpaceShip;
import org.demo.repository.SpaceShipRepository;
import org.demo.service.SpaceShipService;
import org.demo.service.mapper.SpaceShipMapper;
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

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SpaceShipServiceTest {

    @Mock
    private SpaceShipRepository spaceShipRepository;

    @InjectMocks
    private SpaceShipService spaceShipService;

    private static final int TEST_ID = 1;
    private static final String TEST_NAME = "Test SpaceShip";
    private static final String TEST_MOVIE = "Test Movie";
    private static final SpaceShip TEST_SPACE_SHIP_DTO = new SpaceShip(TEST_ID, TEST_NAME, TEST_MOVIE);

    @BeforeEach
    public void setup() {
        // Resetear mocks antes de cada prueba
        reset(spaceShipRepository);
    }

    @Test
    public void testGetSpaceShips() {
        Page<SpaceShip> page = new PageImpl<>(Collections.singletonList(TEST_SPACE_SHIP_DTO));
        when(spaceShipRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(spaceShipRepository.count()).thenReturn(1L);
        Page<SpaceShipDto> result = spaceShipService.getSpaceShips(Pageable.unpaged());
        Assertions.assertEquals(1, result.getContent().size());
        Assertions.assertEquals(TEST_NAME, result.getContent().getFirst().getName());
        Assertions.assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testGetSpaceShipById() {
        when(spaceShipRepository.findById(eq(TEST_ID))).thenReturn(Optional.of(TEST_SPACE_SHIP_DTO));
        SpaceShipDto result = spaceShipService.getSpaceShipById(TEST_ID);
        Assertions.assertEquals(TEST_ID, result.getId());
        Assertions.assertEquals(TEST_NAME, result.getName());
    }

    @Test
    public void testGetSpaceShipById_NotFound() {
        when(spaceShipRepository.findById(eq(TEST_ID))).thenReturn(Optional.empty());
        Assertions.assertThrows(ServiceException.class, () -> spaceShipService.getSpaceShipById(TEST_ID));
    }

    @Test
    public void testGetSpaceShipByName() {
        when(spaceShipRepository.findByNameContaining(eq(TEST_NAME))).thenReturn(Collections.singleton(TEST_SPACE_SHIP_DTO));

        SpaceShipDto result = spaceShipService.getSpaceShipByName(TEST_NAME).iterator().next();

        Assertions.assertEquals(TEST_ID, result.getId());
        Assertions.assertEquals(TEST_NAME, result.getName());
    }

    @Test
    public void testSaveSpaceShip() {
        spaceShipService.saveSpaceShip((SpaceShipMapper.MAPPER.mapToDto(TEST_SPACE_SHIP_DTO)));

        verify(spaceShipRepository, times(1)).save(any());
    }

    @Test
    public void testDeleteSpaceShip() {
        when(spaceShipRepository.existsById(eq(TEST_ID))).thenReturn(true);

        spaceShipService.deleteSpaceShip(TEST_ID);

        verify(spaceShipRepository, times(1)).deleteById(eq(TEST_ID));
    }

    @Test
    public void testDeleteSpaceShip_NotFound() {
        when(spaceShipRepository.existsById(eq(TEST_ID))).thenReturn(false);

        Assertions.assertThrows(ServiceException.class, () -> spaceShipService.deleteSpaceShip(TEST_ID));
    }

    @Test
    public void testUpdateSpaceShip() {
        when(spaceShipRepository.findById(eq(TEST_ID))).thenReturn(Optional.of(TEST_SPACE_SHIP_DTO));

        spaceShipService.updateSpaceShip(SpaceShipMapper.MAPPER.mapToDto(TEST_SPACE_SHIP_DTO));

        verify(spaceShipRepository, times(1)).save(any());
    }

    @Test
    public void testUpdateSpaceShip_NotFound() {
        when(spaceShipRepository.findById(eq(TEST_ID))).thenReturn(Optional.empty());

        Assertions.assertThrows(ServiceException.class, () -> spaceShipService.updateSpaceShip((SpaceShipMapper.MAPPER.mapToDto(TEST_SPACE_SHIP_DTO))));
    }
}
