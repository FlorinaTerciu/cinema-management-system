package com.cinemasystem.entity;

import com.cinemasystem.entity.enums.SeatType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "row_nr", nullable = false, length = 1)
    private String rowNumber;

    @NotNull(message = "Seat number is required")
    @Min(value = 1, message = "Seat number must be >= 1")
    @Max(value = 15, message = "Seat number must be <= 15")
    @Column(name = "seat_nr", nullable = false)
    private Integer seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_type", nullable = false)
    private SeatType seatType;
}
