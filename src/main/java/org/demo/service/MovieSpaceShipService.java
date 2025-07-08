package org.demo.service;

import lombok.RequiredArgsConstructor;
import org.demo.dto.AuditEventDto;
import org.demo.dto.MovieSpaceShipsDto;
import org.demo.exception.ServiceException;
import org.demo.persistance.repository.MovieSpaceShipRepository;
import org.demo.service.mapper.SpaceShipMapper;
import org.demo.service.rabbitmq.RabbitMQSenderService;
import org.demo.service.telemetry.MetricsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieSpaceShipService {
    private final MovieSpaceShipRepository movieSpaceShipRepository;
    private final RabbitMQSenderService rabbitMQSenderService;
    private final MetricsService metricsService;

    public Page<MovieSpaceShipsDto> getSpaceShips(Pageable pageable) {
        return metricsService.executeWithTracing("getSpaceShips", () -> {
            metricsService.incrementSpaceShipRead();
            var list = movieSpaceShipRepository.findAll(pageable).map(SpaceShipMapper.MAPPER::mapToDto);
            var countConstruction = movieSpaceShipRepository.count();
            return new PageImpl<>(list.getContent(), pageable, countConstruction);
        });
    }

    public Set<MovieSpaceShipsDto> getSpaceShipByName(String name) {
        return metricsService.executeWithTracing("getSpaceShipByName", () -> {
            metricsService.incrementSpaceShipRead();
            var spaceShip = movieSpaceShipRepository.findByNameContaining(name);
            return spaceShip.stream().map(SpaceShipMapper.MAPPER::mapToDto).collect(Collectors.toSet());
        });
    }

    public MovieSpaceShipsDto getSpaceShipById(long id) {
        return metricsService.executeWithTracing("getSpaceShipById", () -> {
            if (id < 0) {
                throw new ServiceException("SpaceShip id must be greater than 0", 400);
            }

            metricsService.incrementSpaceShipRead();
            var spaceShip = movieSpaceShipRepository.findById(id).orElse(null);
            if (spaceShip == null) {
                throw new ServiceException("SpaceShip with id " + id + " does not exist", 404);
            }
            return SpaceShipMapper.MAPPER.mapToDto(spaceShip);
        });
    }

    public void saveSpaceShip(MovieSpaceShipsDto movieSpaceShipsDto) {
        metricsService.executeWithTracing("saveSpaceShip", () -> {
            var spaceShip = SpaceShipMapper.MAPPER.mapToEntity(movieSpaceShipsDto);
            spaceShip.setId(null);
            var savedSpaceShip = movieSpaceShipRepository.save(spaceShip);

            metricsService.incrementSpaceShipCreated();

            rabbitMQSenderService.sendAuditMessage(AuditEventDto.builder()
                    .user(SecurityContextHolder.getContext().getAuthentication().getName())
                    .shipId(savedSpaceShip.getId())
                    .shipName(savedSpaceShip.getName())
                    .operation("CREATE")
                    .timestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
                    .build());

            metricsService.incrementAuditMessagesSent();
        });
    }

    public void deleteSpaceShip(long id) {
        metricsService.executeWithTracing("deleteSpaceShip", () -> {
            if (id < 0) {
                throw new ServiceException("SpaceShip id must be greater than 0", 400);
            }

            var spaceShip = movieSpaceShipRepository.findById(id).orElseThrow(() ->
                new ServiceException("SpaceShip with id " + id + " does not exist", 404));

            movieSpaceShipRepository.deleteById(id);
            metricsService.incrementSpaceShipDeleted();

            rabbitMQSenderService.sendAuditMessage(AuditEventDto.builder()
                    .user(SecurityContextHolder.getContext().getAuthentication().getName())
                    .shipId(spaceShip.getId())
                    .shipName(spaceShip.getName())
                    .operation("DELETE")
                    .timestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
                    .build());

            metricsService.incrementAuditMessagesSent();
        });
    }

    public void updateSpaceShip(MovieSpaceShipsDto movieSpaceShipsDto) {
        metricsService.executeWithTracing("updateSpaceShip", () -> {
            if (movieSpaceShipsDto.getId() < 0) {
                throw new ServiceException("SpaceShip id must be greater than 0", 400);
            }
            var spaceShip = movieSpaceShipRepository.findById(movieSpaceShipsDto.getId()).orElse(null);
            if (spaceShip == null) {
                throw new ServiceException("SpaceShip with id " + movieSpaceShipsDto.getId() + " does not exist", 404);
            }
            var updatedSpaceShip = SpaceShipMapper.MAPPER.mapToEntity(movieSpaceShipsDto);
            movieSpaceShipRepository.save(updatedSpaceShip);

            metricsService.incrementSpaceShipUpdated();

            rabbitMQSenderService.sendAuditMessage(AuditEventDto.builder()
                    .user(SecurityContextHolder.getContext().getAuthentication().getName())
                    .shipId(spaceShip.getId())
                    .shipName(spaceShip.getName())
                    .operation("UPDATE")
                    .timestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
                    .build());

            metricsService.incrementAuditMessagesSent();
        });
    }
}
