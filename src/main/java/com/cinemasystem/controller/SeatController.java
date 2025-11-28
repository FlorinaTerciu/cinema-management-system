package com.cinemasystem.controller;

import com.cinemasystem.dto.SeatDTO;
import com.cinemasystem.service.SeatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    @PostMapping
    public ResponseEntity<SeatDTO> create(@Valid @RequestBody SeatDTO dto) {
        SeatDTO created = seatService.create(dto);
        return ResponseEntity
                .created(URI.create("/api/seats/" + created.getId()))
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeatDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody SeatDTO dto) {
        return ResponseEntity.ok(seatService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        seatService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeatDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(seatService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<SeatDTO>> getAll() {
        return ResponseEntity.ok(seatService.getAll());
    }
}
