package com.cinemasystem.entity;

import com.cinemasystem.entity.enums.SeatType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(
        uniqueConstraints = @UniqueConstraint(
                name = "uk_seat_room_row_seat",
                columnNames = {"room_id", "row_nr", "seat_nr"}
        )
)
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "row_nr", nullable = false, length = 1)
    private String rowNumber;

    @Column(name = "seat_nr", nullable = false)
    private Integer seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_type", nullable = false)
    private SeatType seatType;
}
