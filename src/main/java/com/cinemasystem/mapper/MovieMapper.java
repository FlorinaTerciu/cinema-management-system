package com.cinemasystem.mapper;

import com.cinemasystem.dto.MovieDTO;
import com.cinemasystem.entity.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface MovieMapper {

    MovieDTO toDto(Movie entity);

    Movie toEntity(MovieDTO dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(MovieDTO dto, @MappingTarget Movie.MovieBuilder builder);
}
