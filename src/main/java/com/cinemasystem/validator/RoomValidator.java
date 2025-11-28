package com.cinemasystem.validator;

import com.cinemasystem.dto.RoomDTO;
import com.cinemasystem.exception.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class RoomValidator {

    public void validateForCreate(RoomDTO dto) {
        if (dto.getId() != null) {
            throw new BadRequestException("ID must not be provided for create");
        }
    }

    public void validateForUpdate(Long pathId, RoomDTO dto) {
        if (dto.getId() != null && !dto.getId().equals(pathId)) {
            throw new BadRequestException("Path id must match DTO id");
        }
    }
}
