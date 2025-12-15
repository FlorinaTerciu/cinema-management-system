package com.cinemasystem.service;

import com.cinemasystem.dto.UserDTO;
import com.cinemasystem.entity.User;
import com.cinemasystem.entity.enums.Role;
import com.cinemasystem.exception.BadRequestException;
import com.cinemasystem.exception.NotFoundException;
import com.cinemasystem.mapper.UserMapper;
import com.cinemasystem.repository.UserRepository;
import com.cinemasystem.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDTO getCurrentProfile() {
        User user = getOrCreateCurrentUser();
        return userMapper.toDto(user);
    }

    public UserDTO updateCurrentProfile(UserDTO dto) {
        User existing = getOrCreateCurrentUser();
        JwtAuthenticationToken auth = getCurrentAuthentication();

        if (dto.getFamilyName() == null || dto.getFamilyName().isBlank())
            throw new BadRequestException("Family name must not be blank");

        if (dto.getGivenName() == null || dto.getGivenName().isBlank())
            throw new BadRequestException("Given name must not be blank");

        if (dto.getEmail() == null || dto.getEmail().isBlank())
            throw new BadRequestException("Email must not be blank");

        Role roleFromToken = extractRoleFromAuthorities(auth.getAuthorities());

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

    private User getOrCreateCurrentUser() {
        JwtAuthenticationToken auth = getCurrentAuthentication();
        String username = getUsernameFromToken(auth);

        return userRepository.findByUsername(username)
                .orElseGet(() -> autoCreateFromToken(auth, username));
    }

    private User autoCreateFromToken(JwtAuthenticationToken auth, String username) {

        String email = auth.getToken().getClaimAsString("email");
        String givenName = auth.getToken().getClaimAsString("given_name");
        String familyName = auth.getToken().getClaimAsString("family_name");

        if (email == null || email.isBlank())
            throw new IllegalStateException("Keycloak token missing email");

        if (givenName == null || givenName.isBlank())
            throw new IllegalStateException("Keycloak token missing given_name");

        if (familyName == null || familyName.isBlank())
            throw new IllegalStateException("Keycloak token missing family_name");

        Role role = extractRoleFromAuthorities(auth.getAuthorities());

        User user = User.builder()
                .username(username)
                .email(email)
                .givenName(givenName)
                .familyName(familyName)
                .role(role)
                .build();

        return userRepository.save(user);
    }

    private JwtAuthenticationToken getCurrentAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof JwtAuthenticationToken token)) {
            throw new IllegalStateException("No JWT authentication found");
        }
        return token;
    }

    private String getUsernameFromToken(JwtAuthenticationToken auth) {
        String preferred = auth.getToken().getClaimAsString("preferred_username");

        if (preferred != null && !preferred.isBlank())
            return preferred;

        return auth.getName();
    }

    private Role extractRoleFromAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .filter(auth -> auth.equals("EMPLOYEE") || auth.equals("CUSTOMER"))
                .findFirst()
                .map(Role::valueOf)
                .orElse(Role.CUSTOMER);
    }
}