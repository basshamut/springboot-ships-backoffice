package org.demo.service.mapper;

import org.demo.dto.MovieSpaceShipsDto;
import org.demo.repository.MovieSpaceShip;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", imports = {MovieSpaceShip.class, MovieSpaceShipsDto.class})
public interface SpaceShipMapper {
    SpaceShipMapper MAPPER = Mappers.getMapper(SpaceShipMapper.class);

    MovieSpaceShipsDto mapToDto(MovieSpaceShip entity);

    MovieSpaceShip mapToEntity(MovieSpaceShipsDto mallDto);

}
