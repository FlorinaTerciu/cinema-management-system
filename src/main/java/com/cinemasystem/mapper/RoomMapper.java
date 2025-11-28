package com.cinemasystem.mapper;

import com.cinemasystem.dto.RoomDTO;
import com.cinemasystem.entity.Room;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring"
)
public interface RoomMapper {

    RoomDTO toDto(Room entity);

    Room toEntity(RoomDTO dto);

}

