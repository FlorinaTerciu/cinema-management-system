package com.cinemasystem.mapper;

import com.cinemasystem.dto.UserDTO;
import com.cinemasystem.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDto(User entity);

    User toEntity(UserDTO dto);
}
