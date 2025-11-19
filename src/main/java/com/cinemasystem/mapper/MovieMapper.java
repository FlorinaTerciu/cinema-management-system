package com.cinemasystem.mapper;

import com.cinemasystem.dto.MovieDTO;
import com.cinemasystem.entity.Movie;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {

    public MovieDTO toDto(Movie movie) {
        if (movie == null) return null;

        return MovieDTO.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .description(movie.getDescription())
                .durationMinutes(movie.getDurationMinutes())
                .genre(movie.getGenre())
                .rating(movie.getRating())
                .releaseDate(movie.getReleaseDate())
                .posterUrl(movie.getPosterUrl())
                .build();
    }

    public Movie toEntity(MovieDTO dto) {
        if (dto == null) return null;

        return Movie.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .durationMinutes(dto.getDurationMinutes())
                .genre(dto.getGenre())
                .rating(dto.getRating())
                .releaseDate(dto.getReleaseDate())
                .posterUrl(dto.getPosterUrl())
                .build();
    }

    public void updateEntity(Movie movie, MovieDTO dto) {
        movie.setTitle(dto.getTitle());
        movie.setDescription(dto.getDescription());
        movie.setDurationMinutes(dto.getDurationMinutes());
        movie.setGenre(dto.getGenre());
        movie.setRating(dto.getRating());
        movie.setReleaseDate(dto.getReleaseDate());
        movie.setPosterUrl(dto.getPosterUrl());
    }
}