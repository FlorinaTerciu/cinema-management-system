package com.cinemasystem.controller;

import com.cinemasystem.dto.TicketDTO;
import com.cinemasystem.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/reserve")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<TicketDTO> reserve(
            @RequestParam Long showtimeId,
            @RequestParam Long seatId
    ) {
        TicketDTO dto = ticketService.reserveTicket(showtimeId, seatId, getUsername());
        return ResponseEntity.created(URI.create("/api/tickets/" + dto.getId())).body(dto);
    }

    @PostMapping("/buy-now")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<TicketDTO> buyNow(
            @RequestParam Long showtimeId,
            @RequestParam Long seatId
    ) {
        TicketDTO dto = ticketService.buyNow(showtimeId, seatId, getUsername());
        return ResponseEntity.created(URI.create("/api/tickets/" + dto.getId())).body(dto);
    }

    @PutMapping("/{ticketId}/purchase")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<TicketDTO> markAsPurchased(@PathVariable Long ticketId) {
        return ResponseEntity.ok(ticketService.markAsPurchased(ticketId));
    }

    @PutMapping("/{ticketId}/cancel")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<TicketDTO> cancel(@PathVariable Long ticketId) {
        return ResponseEntity.ok(
                ticketService.cancelTicket(ticketId, getUsername())
        );
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<List<TicketDTO>> getMyTickets() {
        return ResponseEntity.ok(
                ticketService.getMyTickets(getUsername())
        );
    }

    private String getUsername() {
        JwtAuthenticationToken auth =
                (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        String preferred = auth.getToken().getClaimAsString("preferred_username");
        return (preferred != null && !preferred.isBlank())
                ? preferred
                : auth.getName();
    }
}
