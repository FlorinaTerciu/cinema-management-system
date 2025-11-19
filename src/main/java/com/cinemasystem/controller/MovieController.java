package com.cinemasystem.controller;

import com.cinemasystem.dto.MovieDTO;
import com.cinemasystem.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @PostMapping
    public ResponseEntity<MovieDTO> create(@RequestBody MovieDTO dto) {
        MovieDTO created = movieService.create(dto);
        return ResponseEntity
                .created(URI.create("/api/movies/" + created.getId()))
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieDTO> update(
            @PathVariable Long id,
            @RequestBody MovieDTO dto) {
        return ResponseEntity.ok(movieService.update(id, dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MovieDTO> partialUpdate(@PathVariable Long id, @RequestBody MovieDTO dto) {
        return ResponseEntity.ok(movieService.partialUpdate(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        movieService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<MovieDTO>> getAll() {
        return ResponseEntity.ok(movieService.getAll());
    }
}
