package org.demo.service;

import lombok.RequiredArgsConstructor;
import org.demo.dto.SpaceShipDto;
import org.demo.exception.ServiceException;
import org.demo.repository.SpaceShipRepository;
import org.demo.service.mapper.SpaceShipMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpaceShipService {
    private final SpaceShipRepository spaceShipRepository;

    public Page<SpaceShipDto> getSpaceShips(Pageable pageable) {
        var list = spaceShipRepository.findAll(pageable).map(SpaceShipMapper.MAPPER::mapToDto);
        var countConstruction = spaceShipRepository.count();
        return new PageImpl<>(list.getContent(), pageable, countConstruction);
    }

    public Set<SpaceShipDto> getSpaceShipByName(String name) {
        var spaceShip = spaceShipRepository.findByNameContaining(name);
        return spaceShip.stream().map(SpaceShipMapper.MAPPER::mapToDto).collect(Collectors.toSet());
    }

    public SpaceShipDto getSpaceShipById(int id) {
        if (id < 0) {
            throw new ServiceException("SpaceShip id must be greater than 0", 400);
        }

        var spaceShip = spaceShipRepository.findById(id).orElse(null);
        if (spaceShip == null) {
            throw new ServiceException("SpaceShip with id " + id + " does not exist", 404);
        }
        return SpaceShipMapper.MAPPER.mapToDto(spaceShip);
    }

    public void saveSpaceShip(SpaceShipDto spaceShipDto) {
        var spaceShip = SpaceShipMapper.MAPPER.mapToEntity(spaceShipDto);
        spaceShip.setId(null);
        spaceShipRepository.save(spaceShip);
    }

    public void deleteSpaceShip(int id) {
        if (id < 0) {
            throw new ServiceException("SpaceShip id must be greater than 0", 400);
        }

        if (!spaceShipRepository.existsById(id)) {
            throw new ServiceException("SpaceShip with id " + id + " does not exist", 404);
        }
        spaceShipRepository.deleteById(id);
    }

    public void updateSpaceShip(SpaceShipDto spaceShipDto) {
        if (spaceShipDto.getId() < 0) {
            throw new ServiceException("SpaceShip id must be greater than 0", 400);
        }
        var spaceShip = spaceShipRepository.findById(spaceShipDto.getId()).orElse(null);
        if (spaceShip == null) {
            throw new ServiceException("SpaceShip with id " + spaceShipDto.getId() + " does not exist", 404);
        }
        var updatedSpaceShip = SpaceShipMapper.MAPPER.mapToEntity(spaceShipDto);
        spaceShipRepository.save(updatedSpaceShip);
    }
}
