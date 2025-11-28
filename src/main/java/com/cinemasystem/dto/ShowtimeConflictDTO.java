package com.cinemasystem.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowtimeConflictDTO {

    private Long showtimeId;
    private Long movieId;
    private Long roomId;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String message;
}
