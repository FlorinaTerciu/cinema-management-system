package com.cinemasystem.dto;

import com.cinemasystem.entity.enums.TicketStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDTO {

    private Long id;

    @NotNull(message = "User id must not be null")
    private Long userId;

    @NotNull(message = "Showtime id must not be null")
    private Long showtimeId;

    @NotNull(message = "Seat id must not be null")
    private Long seatId;

    @NotNull(message = "Price must not be null")
    @DecimalMin(value = "0.0", message = "Price must be >= 0")
    private Double price;

    private LocalDateTime purchaseTime;

    private TicketStatus status;
}
