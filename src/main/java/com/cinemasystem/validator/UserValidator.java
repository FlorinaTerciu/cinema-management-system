package com.cinemasystem.validator;

import com.cinemasystem.dto.UserDTO;
import com.cinemasystem.exception.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    public void validateForCreate(UserDTO dto) {
        if (dto.getId() != null) {
            throw new BadRequestException("ID must not be provided for create");
        }
    }

    public void validateForUpdate(Long pathId, UserDTO dto) {
        if (dto.getId() != null && !dto.getId().equals(pathId)) {
            throw new BadRequestException("Path id must match DTO id");
        }
    }
}
