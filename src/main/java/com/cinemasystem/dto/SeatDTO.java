package com.cinemasystem.dto;

import com.cinemasystem.entity.enums.SeatType;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatDTO {

    private Long id;

    @NotNull(message = "Room id must not be null")
    private Long roomId;

    @NotBlank(message = "Row number must not be blank")
    @Pattern(regexp = "^[A-J]$", message = "Row must be a letter between A and J")
    private String rowNumber;

    @NotNull(message = "Seat number is required")
    @Min(value = 1, message = "Seat number must be >= 1")
    @Max(value = 15, message = "Seat number must be <= 15")
    private Integer seatNumber;

    @NotNull(message = "Seat type must not be null")
    private SeatType seatType;
}

