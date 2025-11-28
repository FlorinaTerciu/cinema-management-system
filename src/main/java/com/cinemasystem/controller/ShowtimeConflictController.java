package com.cinemasystem.controller;

import com.cinemasystem.dto.ShowtimeConflictDTO;
import com.cinemasystem.service.ShowtimeConflictService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/showtimes/conflicts")
@RequiredArgsConstructor
public class ShowtimeConflictController {

    private final ShowtimeConflictService conflictService;

    @GetMapping
    public ResponseEntity<List<ShowtimeConflictDTO>> getAllConflicts() {
        return ResponseEntity.ok(conflictService.getAllConflicts());
    }
}
