package com.cinemasystem.service;

import com.cinemasystem.dto.MovieDTO;
import com.cinemasystem.dto.ShowtimeConflictDTO;
import com.cinemasystem.entity.Movie;
import com.cinemasystem.events.MovieDurationChangedEvent;
import com.cinemasystem.exception.NotFoundException;
import com.cinemasystem.mapper.MovieMapper;
import com.cinemasystem.repository.MovieRepository;
import com.cinemasystem.validator.MovieValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final MovieValidator movieValidator;
    private final ApplicationEventPublisher eventPublisher;
    private final ShowtimeConflictService conflictService;

    public MovieDTO create(MovieDTO dto) {
        movieValidator.validateForCreate(dto);
        Movie saved = movieRepository.save(movieMapper.toEntity(dto));
        return movieMapper.toDto(saved);
    }

    public MovieDTO update(Long id, MovieDTO dto) {
        movieValidator.validateForUpdate(id, dto);
        Movie existing = getMovie(id);

        boolean durationChanged = isDurationChanged(dto.getDurationMinutes(), existing.getDurationMinutes());

        Movie updatedEntity = movieMapper.toEntity(dto)
                .toBuilder()
                .id(existing.getId())
                .build();

        Movie saved = movieRepository.save(updatedEntity);

        return buildResponseAfterUpdate(saved, durationChanged);
    }

    public MovieDTO partialUpdate(Long id, MovieDTO dto) {

        Movie existing = getMovie(id);
        movieValidator.validateForPartial(dto);

        Movie.MovieBuilder builder = existing.toBuilder();
        movieMapper.updateEntityFromDto(dto, builder);
        Movie merged = builder.build();

        boolean durationChanged = isDurationChanged(merged.getDurationMinutes(), existing.getDurationMinutes());

        Movie saved = movieRepository.save(merged);

        return buildResponseAfterUpdate(saved, durationChanged);
    }

    private MovieDTO buildResponseAfterUpdate(Movie saved, boolean durationChanged) {

        MovieDTO response = movieMapper.toDto(saved);

        if (durationChanged) {
            eventPublisher.publishEvent(new MovieDurationChangedEvent(saved.getId()));
            List<ShowtimeConflictDTO> warnings = conflictService.getConflictsForMovie(saved.getId());
            response.setWarnings(warnings);
        }

        return response;
    }

    private boolean isDurationChanged(Integer newValue, Integer oldValue) {
        return newValue != null && !newValue.equals(oldValue);
    }

    private Movie getMovie(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Movie not found with id " + id));
    }

    public void delete(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new NotFoundException("Movie not found with id " + id);
        }
        movieRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public MovieDTO getById(Long id) {
        return movieRepository.findById(id)
                .map(movieMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Movie not found with id " + id));
    }

    @Transactional(readOnly = true)
    public List<MovieDTO> getAll() {
        return movieRepository.findAll().stream()
                .map(movieMapper::toDto)
                .toList();
    }
}
