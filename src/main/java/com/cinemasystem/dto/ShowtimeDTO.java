package com.cinemasystem.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowtimeDTO {

    private Long id;

    @NotNull(message = "Movie id must not be null")
    private Long movieId;

    @NotNull(message = "Room id must not be null")
    private Long roomId;

    @NotNull(message = "Start time must not be null")
    @Future(message = "Start time must be in the future")
    private LocalDateTime startTime;

    private LocalDateTime endTime; // calculated by backend
}

