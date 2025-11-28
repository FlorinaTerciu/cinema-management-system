package com.cinemasystem.controller;

import com.cinemasystem.dto.ShowtimeDTO;
import com.cinemasystem.service.ShowtimeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/showtimes")
@RequiredArgsConstructor
public class ShowtimeController {

    private final ShowtimeService showtimeService;

    @PostMapping
    public ResponseEntity<ShowtimeDTO> create(@Valid @RequestBody ShowtimeDTO dto) {
        ShowtimeDTO created = showtimeService.create(dto);
        return ResponseEntity.created(URI.create("/api/showtimes/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShowtimeDTO> update(@PathVariable Long id, @Valid @RequestBody ShowtimeDTO dto) {
        return ResponseEntity.ok(showtimeService.update(id, dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ShowtimeDTO> partialUpdate(
            @PathVariable Long id,
            @RequestBody ShowtimeDTO dto) {

        return ResponseEntity.ok(showtimeService.partialUpdate(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        showtimeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowtimeDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(showtimeService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ShowtimeDTO>> getAll() {
        return ResponseEntity.ok(showtimeService.getAll());
    }
}
