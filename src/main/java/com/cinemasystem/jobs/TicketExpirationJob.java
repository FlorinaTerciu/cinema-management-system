package com.cinemasystem.jobs;

import com.cinemasystem.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketExpirationJob {

    private final TicketService ticketService;

    /**
     * Deletes RESERVED tickets that were not purchased
     * before the allowed cashier window.
     */
    @Scheduled(fixedRate = 900_000)
    public void deleteExpiredReservations() {
        ticketService.deleteExpiredReservations();
    }
}
