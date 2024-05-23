package org.demo.service.mapper;

import org.demo.dto.SpaceShipDto;
import org.demo.repository.SpaceShip;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", imports = {SpaceShip.class, SpaceShipDto.class})
public interface SpaceShipMapper {
    SpaceShipMapper MAPPER = Mappers.getMapper(SpaceShipMapper.class);

    SpaceShipDto mapToDto(SpaceShip entity);

    SpaceShip mapToEntity(SpaceShipDto mallDto);

}
