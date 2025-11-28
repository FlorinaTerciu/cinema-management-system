package com.cinemasystem.service;

import com.cinemasystem.dto.ShowtimeDTO;
import com.cinemasystem.entity.Movie;
import com.cinemasystem.entity.Room;
import com.cinemasystem.entity.Showtime;
import com.cinemasystem.events.MovieDurationChangedEvent;
import com.cinemasystem.exception.BadRequestException;
import com.cinemasystem.exception.NotFoundException;
import com.cinemasystem.mapper.ShowtimeMapper;
import com.cinemasystem.repository.MovieRepository;
import com.cinemasystem.repository.RoomRepository;
import com.cinemasystem.repository.ShowtimeRepository;
import com.cinemasystem.validator.ShowtimeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ShowtimeService {

    private static final int BUFFER_MINUTES = 20;

    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    private final ShowtimeMapper showtimeMapper;
    private final ShowtimeValidator showtimeValidator;

    public ShowtimeDTO create(ShowtimeDTO dto) {
        showtimeValidator.validateForCreate(dto);

        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new NotFoundException("Movie not found with id " + dto.getMovieId()));

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new NotFoundException("Room not found with id " + dto.getRoomId()));

        LocalDateTime endTime = calculateEndTime(dto.getStartTime(), movie.getDurationMinutes());

        ensureNoOverlap(room.getId(), dto.getStartTime(), endTime, null);

        Showtime entity = showtimeMapper.toEntity(dto)
                .toBuilder()
                .movie(movie)
                .room(room)
                .endTime(endTime)
                .build();

        Showtime saved = showtimeRepository.save(entity);
        return showtimeMapper.toDto(saved);
    }

    public ShowtimeDTO update(Long id, ShowtimeDTO dto) {
        showtimeValidator.validateForUpdate(id, dto);

        Showtime existing = showtimeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Showtime not found with id " + id));

        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new NotFoundException("Movie not found with id " + dto.getMovieId()));

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new NotFoundException("Room not found with id " + dto.getRoomId()));

        LocalDateTime endTime = calculateEndTime(dto.getStartTime(), movie.getDurationMinutes());

        ensureNoOverlap(room.getId(), dto.getStartTime(), endTime, id);

        Showtime updated = showtimeMapper.toEntity(dto)
                .toBuilder()
                .id(existing.getId())
                .movie(movie)
                .room(room)
                .endTime(endTime)
                .build();

        Showtime saved = showtimeRepository.save(updated);
        return showtimeMapper.toDto(saved);
    }

    public ShowtimeDTO partialUpdate(Long id, ShowtimeDTO dto) {

        showtimeValidator.validateForPartial(dto);

        Showtime existing = showtimeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Showtime not found with id " + id));

        Long movieId = dto.getMovieId() != null ? dto.getMovieId() : existing.getMovie().getId();
        Long roomId = dto.getRoomId() != null ? dto.getRoomId() : existing.getRoom().getId();
        LocalDateTime startTime = dto.getStartTime() != null ? dto.getStartTime() : existing.getStartTime();

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new NotFoundException("Movie not found with id " + movieId));

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException("Room not found with id " + roomId));

        LocalDateTime endTime = calculateEndTime(startTime, movie.getDurationMinutes());

        ensureNoOverlap(roomId, startTime, endTime, id);

        Showtime updated = existing.toBuilder()
                .movie(movie)
                .room(room)
                .startTime(startTime)
                .endTime(endTime)
                .build();

        Showtime saved = showtimeRepository.save(updated);

        return showtimeMapper.toDto(saved);
    }


    public void delete(Long id) {
        if (!showtimeRepository.existsById(id)) {
            throw new NotFoundException("Showtime not found with id " + id);
        }
        showtimeRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public ShowtimeDTO getById(Long id) {
        return showtimeRepository.findById(id)
                .map(showtimeMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Showtime not found with id " + id));
    }

    @Transactional(readOnly = true)
    public List<ShowtimeDTO> getAll() {
        return showtimeRepository.findAll()
                .stream()
                .map(showtimeMapper::toDto)
                .toList();
    }

    private LocalDateTime calculateEndTime(LocalDateTime start, int durationMinutes) {
        return start
                .plusMinutes(durationMinutes)
                .plusMinutes(BUFFER_MINUTES);
    }

    private void ensureNoOverlap(Long roomId,
                                 LocalDateTime start,
                                 LocalDateTime end,
                                 Long currentShowtimeId) {

        List<Showtime> existing = showtimeRepository.findByRoomId(roomId);

        for (Showtime s : existing) {

            if (currentShowtimeId != null && s.getId().equals(currentShowtimeId))
                continue;

            boolean overlaps =
                    s.getEndTime().isAfter(start) &&
                            s.getStartTime().isBefore(end);

            if (overlaps)
                throw new BadRequestException("Showtime overlaps with an existing showtime in this room");
        }
    }

    @EventListener
    public void handleMovieDurationChanged(MovieDurationChangedEvent event) {
        Movie movie = movieRepository.findById(event.movieId())
                .orElseThrow(() -> new NotFoundException("Movie not found with id " + event.movieId()));

        List<Showtime> showtimes = showtimeRepository.findByMovieId(event.movieId());

        for (Showtime s : showtimes) {
            LocalDateTime newEnd = calculateEndTime(s.getStartTime(), movie.getDurationMinutes());

            Showtime updated = s.toBuilder()
                    .endTime(newEnd)
                    .build();

            showtimeRepository.save(updated);
        }
    }
}
