package com.cinemasystem.mapper;

import com.cinemasystem.dto.ShowtimeDTO;
import com.cinemasystem.entity.Showtime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShowtimeMapper {

    @Mapping(source = "movie.id", target = "movieId")
    @Mapping(source = "room.id", target = "roomId")
    ShowtimeDTO toDto(Showtime entity);

    @Mapping(target = "movie", ignore = true)
    @Mapping(target = "room", ignore = true)
    @Mapping(target = "endTime", ignore = true)
    Showtime toEntity(ShowtimeDTO dto);
}