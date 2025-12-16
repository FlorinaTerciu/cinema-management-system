package com.cinemasystem.service;

import com.cinemasystem.dto.SeatDTO;
import com.cinemasystem.entity.Showtime;
import com.cinemasystem.entity.enums.TicketStatus;
import com.cinemasystem.exception.NotFoundException;
import com.cinemasystem.mapper.SeatMapper;
import com.cinemasystem.repository.SeatRepository;
import com.cinemasystem.repository.ShowtimeRepository;
import com.cinemasystem.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeatAvailabilityService {

    private final SeatRepository seatRepository;
    private final TicketRepository ticketRepository;
    private final ShowtimeRepository showtimeRepository;
    private final SeatMapper seatMapper;

    public List<SeatDTO> getAvailableSeats(Long showtimeId) {

        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new NotFoundException("Showtime not found"));

        List<Long> blockedSeatIds = ticketRepository
                .findByShowtimeIdAndStatusIn(
                        showtimeId,
                        List.of(TicketStatus.RESERVED, TicketStatus.PURCHASED)
                )
                .stream()
                .map(t -> t.getSeat().getId())
                .toList();

        return seatRepository.findByRoomId(showtime.getRoom().getId()).stream()
                .filter(seat -> !blockedSeatIds.contains(seat.getId()))
                .map(seatMapper::toDto)
                .toList();
    }
}
