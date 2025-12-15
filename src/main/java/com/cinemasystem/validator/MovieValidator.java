package com.cinemasystem.validator;

import com.cinemasystem.dto.MovieDTO;
import com.cinemasystem.exception.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class MovieValidator {

    public void validateForCreate(MovieDTO dto) {
        if (dto.getId() != null) {
            throw new BadRequestException("ID must not be provided for create");
        }
    }

    public void validateForUpdate(Long pathId, MovieDTO dto) {
        if (dto.getId() != null && !dto.getId().equals(pathId)) {
            throw new BadRequestException("Path id must match body id");
        }
    }

    public void validateForPartial(MovieDTO dto) {

        if (dto.getTitle() != null && dto.getTitle().isBlank()) {
            throw new BadRequestException("Title must not be blank");
        }

        if (dto.getGenre() != null && dto.getGenre().isBlank()) {
            throw new BadRequestException("Genre must not be blank");
        }

        if (dto.getPosterUrl() != null && dto.getPosterUrl().isBlank()) {
            throw new BadRequestException("Poster URL must not be blank");
        }

        if (dto.getDurationMinutes() != null && dto.getDurationMinutes() < 1) {
            throw new BadRequestException("Duration must be >= 1");
        }

        if (dto.getRating() != null &&
                (dto.getRating() < 0.0 || dto.getRating() > 10.0)) {
            throw new BadRequestException("Rating must be between 0 and 10");
        }
    }
}
