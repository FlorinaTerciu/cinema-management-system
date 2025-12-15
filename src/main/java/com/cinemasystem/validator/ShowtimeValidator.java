package com.cinemasystem.validator;

import com.cinemasystem.dto.ShowtimeDTO;
import com.cinemasystem.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ShowtimeValidator {

    public void validateForCreate(ShowtimeDTO dto) {
        if (dto.getId() != null) {
            throw new BadRequestException("ID must not be provided for create");
        }
    }

    public void validateForUpdate(Long pathId, ShowtimeDTO dto) {
        if (dto.getId() != null && !dto.getId().equals(pathId)) {
            throw new BadRequestException("Path id must match DTO id");
        }
    }

    public void validateForPartial(ShowtimeDTO dto) {
        if (dto.getId() != null) {
            throw new BadRequestException("ID must not be provided for partial update");
        }
        if (dto.getStartTime() != null && !dto.getStartTime().isAfter(LocalDateTime.now())) {
            throw new BadRequestException("Start time must be in the future");
        }
    }

}
