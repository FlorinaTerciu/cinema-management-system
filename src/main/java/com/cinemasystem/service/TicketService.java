package com.cinemasystem.service;

import com.cinemasystem.dto.TicketDTO;
import com.cinemasystem.entity.*;
import com.cinemasystem.entity.enums.SeatType;
import com.cinemasystem.entity.enums.TicketStatus;
import com.cinemasystem.exception.NotFoundException;
import com.cinemasystem.mapper.TicketMapper;
import com.cinemasystem.repository.*;
import com.cinemasystem.validator.TicketValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketService {

    private static final double BASE_PRICE = 25.0;
    private static final double VIP_MULTIPLIER = 1.3;
    private static final int CASH_LIMIT_MINUTES = 30;

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;
    private final TicketMapper ticketMapper;
    private final TicketValidator ticketValidator;

    public TicketDTO reserveTicket(Long showtimeId, Long seatId, String username) {

        User user = getUser(username);
        Showtime showtime = getShowtime(showtimeId);
        Seat seat = getSeat(seatId);

        ticketValidator.validateReservationAllowed(showtime);

        boolean blocked = ticketRepository.existsByShowtimeIdAndSeatIdAndStatusIn(
                showtimeId,
                seatId,
                List.of(TicketStatus.RESERVED, TicketStatus.PURCHASED)
        );
        ticketValidator.validateSeatAvailable(blocked);

        Ticket ticket = Ticket.builder()
                .user(user)
                .showtime(showtime)
                .seat(seat)
                .price(calculatePrice(seat))
                .status(TicketStatus.RESERVED)
                .build();

        return ticketMapper.toDto(ticketRepository.save(ticket));
    }

    public TicketDTO buyNow(Long showtimeId, Long seatId, String username) {

        User user = getUser(username);
        Showtime showtime = getShowtime(showtimeId);
        Seat seat = getSeat(seatId);

        boolean blocked = ticketRepository.existsByShowtimeIdAndSeatIdAndStatusIn(
                showtimeId,
                seatId,
                List.of(TicketStatus.RESERVED, TicketStatus.PURCHASED)
        );
        ticketValidator.validateSeatAvailable(blocked);

        Ticket ticket = Ticket.builder()
                .user(user)
                .showtime(showtime)
                .seat(seat)
                .price(calculatePrice(seat))
                .status(TicketStatus.PURCHASED)
                .build();

        return ticketMapper.toDto(ticketRepository.save(ticket));
    }
    public TicketDTO cancelTicket(Long ticketId, String username) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new NotFoundException("Ticket not found"));

        ticketValidator.validateCancelAllowed(ticket, username);

        Ticket cancelled = ticket.toBuilder()
                .status(TicketStatus.CANCELLED)
                .build();

        return ticketMapper.toDto(ticketRepository.save(cancelled));
    }

    public TicketDTO markAsPurchased(Long ticketId) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new NotFoundException("Ticket not found"));

        ticketValidator.validatePurchaseTransition(ticket);

        Ticket updated = ticket.toBuilder()
                .status(TicketStatus.PURCHASED)
                .build();

        return ticketMapper.toDto(ticketRepository.save(updated));
    }

    public void deleteExpiredReservations() {

        LocalDateTime cashierWindowStart  = LocalDateTime.now().plusMinutes(CASH_LIMIT_MINUTES);

        List<Ticket> expired = ticketRepository.findExpiredReservations(
                TicketStatus.RESERVED,
                cashierWindowStart
        );

        ticketRepository.deleteAll(expired);
    }

    @Transactional(readOnly = true)
    public List<TicketDTO> getMyTickets(String username) {

        return ticketRepository
                .findByUserUsernameOrderByPurchaseTimeDesc(username)
                .stream()
                .map(ticketMapper::toDto)
                .toList();
    }


    private double calculatePrice(Seat seat) {
        return seat.getSeatType() == SeatType.VIP
                ? BASE_PRICE * VIP_MULTIPLIER
                : BASE_PRICE;
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    private Showtime getShowtime(Long id) {
        return showtimeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Showtime not found"));
    }

    private Seat getSeat(Long id) {
        return seatRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Seat not found"));
    }
}
