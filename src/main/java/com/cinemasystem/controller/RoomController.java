package com.cinemasystem.controller;

import com.cinemasystem.dto.RoomDTO;
import com.cinemasystem.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomDTO> create(@Valid @RequestBody RoomDTO dto) {
        RoomDTO created = roomService.create(dto);
        return ResponseEntity
                .created(URI.create("/api/rooms/" + created.getId()))
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody RoomDTO dto) {
        return ResponseEntity.ok(roomService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roomService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<RoomDTO>> getAll() {
        return ResponseEntity.ok(roomService.getAll());
    }
}
