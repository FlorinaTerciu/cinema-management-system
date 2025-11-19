package com.cinemasystem.service;

import com.cinemasystem.dto.MovieDTO;
import com.cinemasystem.entity.Movie;
import com.cinemasystem.exception.NotFoundException;
import com.cinemasystem.mapper.MovieMapper;
import com.cinemasystem.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public MovieDTO create(MovieDTO movieDTO) {
        Movie movie = movieMapper.toEntity(movieDTO);
        return movieMapper.toDto(movieRepository.save(movie));
    }

    public MovieDTO update(Long id, MovieDTO movieDTO) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Movie not found with id: " + id));

        movieMapper.updateEntity(movie, movieDTO);
        return movieMapper.toDto(movieRepository.save(movie));
    }

    public MovieDTO partialUpdate(Long id, MovieDTO dto) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Movie not found with id: " + id));

        if (dto.getTitle() != null) movie.setTitle(dto.getTitle());
        if (dto.getDescription() != null) movie.setDescription(dto.getDescription());
        if (dto.getDurationMinutes() != null) movie.setDurationMinutes(dto.getDurationMinutes());
        if (dto.getGenre() != null) movie.setGenre(dto.getGenre());
        if (dto.getRating() != null) movie.setRating(dto.getRating());
        if (dto.getReleaseDate() != null) movie.setReleaseDate(dto.getReleaseDate());
        if (dto.getPosterUrl() != null) movie.setPosterUrl(dto.getPosterUrl());

        return movieMapper.toDto(movieRepository.save(movie));
    }

    public void delete(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new NotFoundException("Movie not found with id: " + id);
        }
        movieRepository.deleteById(id);
    }

    public MovieDTO getById(Long id) {
        return movieRepository.findById(id)
                .map(movieMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Movie not found with id: " + id));
    }

    public List<MovieDTO> getAll() {
        return movieRepository.findAll()
                .stream()
                .map(movieMapper::toDto)
                .toList();
    }
}
