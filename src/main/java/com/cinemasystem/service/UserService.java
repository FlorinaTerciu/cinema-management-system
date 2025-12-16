package com.cinemasystem.service;

import com.cinemasystem.dto.UserDTO;
import com.cinemasystem.entity.User;
import com.cinemasystem.exception.BadRequestException;
import com.cinemasystem.exception.NotFoundException;
import com.cinemasystem.mapper.UserMapper;
import com.cinemasystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public UserDTO getCurrentProfile() {
        User user = getCurrentUser();
        return userMapper.toDto(user);
    }

    public UserDTO updateCurrentProfile(UserDTO dto) {
        User existing = getCurrentUser();

        if (dto.getFamilyName() == null || dto.getFamilyName().isBlank()) {
            throw new BadRequestException("Family name must not be blank");
        }
        if (dto.getGivenName() == null || dto.getGivenName().isBlank()) {
            throw new BadRequestException("Given name must not be blank");
        }
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new BadRequestException("Email must not be blank");
        }

        // username + role remain unchanged
        User updated = existing.toBuilder()
                .familyName(dto.getFamilyName())
                .givenName(dto.getGivenName())
                .email(dto.getEmail())
                .build();

        return userMapper.toDto(userRepository.save(updated));
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserDTO getById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new NotFoundException("User not found with id " + id));
    }

    private User getCurrentUser() {
        JwtAuthenticationToken auth = getCurrentAuthentication();

        String preferred = auth.getToken().getClaimAsString("preferred_username");
        String username = (preferred != null && !preferred.isBlank()) ? preferred : auth.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException(
                        "Authenticated user not provisioned in DB. Check UserProvisioningFilter wiring."
                ));
    }

    private JwtAuthenticationToken getCurrentAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof JwtAuthenticationToken token)) {
            throw new IllegalStateException("No JWT authentication found");
        }
        return token;
    }
}
