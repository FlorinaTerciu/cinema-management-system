package com.cinemasystem.controller;

import com.cinemasystem.dto.SeatDTO;
import com.cinemasystem.service.SeatAvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/showtimes/{showtimeId}")
@RequiredArgsConstructor
public class SeatAvailabilityController {

    private final SeatAvailabilityService availabilityService;

    @GetMapping("/available-seats")
    public ResponseEntity<List<SeatDTO>> getAvailableSeats(@PathVariable Long showtimeId) {
        return ResponseEntity.ok(
                availabilityService.getAvailableSeats(showtimeId)
        );
    }
}
