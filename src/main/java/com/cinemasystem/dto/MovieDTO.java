package com.cinemasystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDTO {

    private Long id;

    @NotBlank(message = "Title must not be blank")
    private String title;

    @Size(max = 2000, message = "Description must be at most 2000 characters")
    private String description;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer durationMinutes;

    @NotBlank(message = "Genre must not be blank")
    private String genre;

    @DecimalMin(value = "0.0", inclusive = true, message = "Rating must be >= 0")
    @DecimalMax(value = "10.0", inclusive = true, message = "Rating must be <= 10")
    private Double rating;

    private LocalDate releaseDate;

    @NotBlank(message = "Poster URL must not be blank")
    private String posterUrl;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ShowtimeConflictDTO> warnings;
}
