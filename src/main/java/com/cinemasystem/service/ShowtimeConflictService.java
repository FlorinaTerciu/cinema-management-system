package com.cinemasystem.service;

import com.cinemasystem.dto.ShowtimeConflictDTO;
import com.cinemasystem.entity.Showtime;
import com.cinemasystem.repository.ShowtimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShowtimeConflictService {

    private final ShowtimeRepository showtimeRepository;

    public List<ShowtimeConflictDTO> getAllConflicts() {
        List<Showtime> all = showtimeRepository.findAll();

        Map<Long, List<Showtime>> byRoom = all.stream()
                .collect(Collectors.groupingBy(s -> s.getRoom().getId()));

        List<ShowtimeConflictDTO> conflicts = new ArrayList<>();

        for (Map.Entry<Long, List<Showtime>> entry : byRoom.entrySet()) {
            List<Showtime> shows = entry.getValue();

            for (int i = 0; i < shows.size(); i++) {
                for (int j = i + 1; j < shows.size(); j++) {

                    Showtime a = shows.get(i);
                    Showtime b = shows.get(j);

                    if (intervalsOverlap(a.getStartTime(), a.getEndTime(),
                            b.getStartTime(), b.getEndTime())) {

                        conflicts.add(buildConflict(a, b));
                        conflicts.add(buildConflict(b, a));
                    }
                }
            }
        }

        return conflicts;
    }

    public List<ShowtimeConflictDTO> getConflictsForMovie(Long movieId) {
        return getAllConflicts().stream()
                .filter(c -> Objects.equals(c.getMovieId(), movieId))
                .toList();
    }

    private boolean intervalsOverlap(LocalDateTime startA, LocalDateTime endA,
                                     LocalDateTime startB, LocalDateTime endB) {
        return endA.isAfter(startB) && startA.isBefore(endB);
    }

    private ShowtimeConflictDTO buildConflict(Showtime a, Showtime b) {

        String startA = a.getStartTime().toLocalTime().toString();
        String endA   = a.getEndTime().toLocalTime().toString();
        String startB = b.getStartTime().toLocalTime().toString();
        String endB   = b.getEndTime().toLocalTime().toString();

        String msg = "Showtime " + a.getId() +
                " (" + startA + "-" + endA + ")" +
                " overlaps with showtime " + b.getId() +
                " (" + startB + "-" + endB + ")" +
                " in room " + a.getRoom().getId();

        return ShowtimeConflictDTO.builder()
                .showtimeId(a.getId())
                .movieId(a.getMovie().getId())
                .roomId(a.getRoom().getId())
                .startTime(a.getStartTime())
                .endTime(a.getEndTime())
                .message(msg)
                .build();
    }

}
