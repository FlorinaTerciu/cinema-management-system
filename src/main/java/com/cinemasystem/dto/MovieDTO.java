package com.cinemasystem.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDTO {

    private Long id;
    private String title;
    private String description;
    private Integer durationMinutes;
    private String genre;
    private Double rating;
    private LocalDate releaseDate;
    private String posterUrl;
}
