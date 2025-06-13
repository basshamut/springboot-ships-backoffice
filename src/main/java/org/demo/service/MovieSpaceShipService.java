package org.demo.service;

import lombok.RequiredArgsConstructor;
import org.demo.dto.MovieSpaceShipsDto;
import org.demo.exception.ServiceException;
import org.demo.repository.MovieSpaceShipRepository;
import org.demo.service.mapper.SpaceShipMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieSpaceShipService {
    private final MovieSpaceShipRepository movieSpaceShipRepository;

    public Page<MovieSpaceShipsDto> getSpaceShips(Pageable pageable) {
        var list = movieSpaceShipRepository.findAll(pageable).map(SpaceShipMapper.MAPPER::mapToDto);
        var countConstruction = movieSpaceShipRepository.count();
        return new PageImpl<>(list.getContent(), pageable, countConstruction);
    }

    public Set<MovieSpaceShipsDto> getSpaceShipByName(String name) {
        var spaceShip = movieSpaceShipRepository.findByNameContaining(name);
        return spaceShip.stream().map(SpaceShipMapper.MAPPER::mapToDto).collect(Collectors.toSet());
    }

    public MovieSpaceShipsDto getSpaceShipById(int id) {
        if (id < 0) {
            throw new ServiceException("SpaceShip id must be greater than 0", 400);
        }

        var spaceShip = movieSpaceShipRepository.findById(id).orElse(null);
        if (spaceShip == null) {
            throw new ServiceException("SpaceShip with id " + id + " does not exist", 404);
        }
        return SpaceShipMapper.MAPPER.mapToDto(spaceShip);
    }

    public void saveSpaceShip(MovieSpaceShipsDto movieSpaceShipsDto) {
        var spaceShip = SpaceShipMapper.MAPPER.mapToEntity(movieSpaceShipsDto);
        spaceShip.setId(null);
        movieSpaceShipRepository.save(spaceShip);
    }

    public void deleteSpaceShip(int id) {
        if (id < 0) {
            throw new ServiceException("SpaceShip id must be greater than 0", 400);
        }

        if (!movieSpaceShipRepository.existsById(id)) {
            throw new ServiceException("SpaceShip with id " + id + " does not exist", 404);
        }
        movieSpaceShipRepository.deleteById(id);
    }

    public void updateSpaceShip(MovieSpaceShipsDto movieSpaceShipsDto) {
        if (movieSpaceShipsDto.getId() < 0) {
            throw new ServiceException("SpaceShip id must be greater than 0", 400);
        }
        var spaceShip = movieSpaceShipRepository.findById(movieSpaceShipsDto.getId()).orElse(null);
        if (spaceShip == null) {
            throw new ServiceException("SpaceShip with id " + movieSpaceShipsDto.getId() + " does not exist", 404);
        }
        var updatedSpaceShip = SpaceShipMapper.MAPPER.mapToEntity(movieSpaceShipsDto);
        movieSpaceShipRepository.save(updatedSpaceShip);
    }
}
