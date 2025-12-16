package com.cinemasystem.validator;

import com.cinemasystem.entity.Showtime;
import com.cinemasystem.entity.Ticket;
import com.cinemasystem.entity.enums.TicketStatus;
import com.cinemasystem.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TicketValidator {

    private static final int CASH_LIMIT_MINUTES = 30;

    public void validateSeatAvailable(boolean seatBlocked) {
        if (seatBlocked) {
            throw new BadRequestException("Seat is already reserved or purchased");
        }
    }

    public void validateReservationAllowed(Showtime showtime) {
        LocalDateTime limit = showtime.getStartTime().minusMinutes(CASH_LIMIT_MINUTES);
        if (LocalDateTime.now().isAfter(limit)) {
            throw new BadRequestException(
                    "Reservations close " + CASH_LIMIT_MINUTES + " minutes before showtime"
            );
        }
    }

    public void validatePurchaseTransition(Ticket ticket) {
        if (ticket.getStatus() != TicketStatus.RESERVED) {
            throw new BadRequestException("Only RESERVED tickets can be purchased");
        }
    }

    public void validateCancelAllowed(Ticket ticket, String username) {

        if (ticket.getStatus() != TicketStatus.RESERVED) {
            throw new BadRequestException("Only RESERVED tickets can be cancelled");
        }

        if (!ticket.getUser().getUsername().equals(username)) {
            throw new BadRequestException("You can cancel only your own tickets");
        }
    }

}
