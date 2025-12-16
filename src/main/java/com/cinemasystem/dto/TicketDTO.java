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

    @NotNull(message = "Showtime id must not be null")
    private Long showtimeId;

    @NotNull(message = "Seat id must not be null")
    private Long seatId;

    private Double price;

    private LocalDateTime purchaseTime;

    private TicketStatus status;
}
