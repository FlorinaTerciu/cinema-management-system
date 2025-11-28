package com.cinemasystem.mapper;

import com.cinemasystem.dto.SeatDTO;
import com.cinemasystem.entity.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SeatMapper {

    @Mapping(source = "room.id", target = "roomId")
    SeatDTO toDto(Seat entity);

    @Mapping(target = "room", ignore = true)
    Seat toEntity(SeatDTO dto);
}

