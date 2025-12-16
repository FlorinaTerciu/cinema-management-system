package com.cinemasystem.security;

import com.cinemasystem.entity.User;
import com.cinemasystem.entity.enums.Role;
import com.cinemasystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserProvisioningService {

    private final UserRepository userRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void provisionIfMissing(JwtAuthenticationToken auth) {

        String username = getUsernameFromToken(auth);
        if (username == null || username.isBlank()) {
            throw new IllegalStateException("Keycloak token missing preferred_username");
        }

        // Already exists -> nothing to do
        if (userRepository.findByUsername(username).isPresent()) {
            return;
        }

        String email = auth.getToken().getClaimAsString("email");
        String givenName = auth.getToken().getClaimAsString("given_name");
        String familyName = auth.getToken().getClaimAsString("family_name");

        if (email == null || email.isBlank()) {
            throw new IllegalStateException("Keycloak token missing email");
        }
        if (givenName == null || givenName.isBlank()) {
            throw new IllegalStateException("Keycloak token missing given_name");
        }
        if (familyName == null || familyName.isBlank()) {
            throw new IllegalStateException("Keycloak token missing family_name");
        }

        Role role = extractRoleFromAuthorities(auth.getAuthorities());

        User user = User.builder()
                .username(username)
                .email(email)
                .givenName(givenName)
                .familyName(familyName)
                .role(role)
                .build();

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            // Another request/thread may have inserted at the same time -> re-check and continue
            userRepository.findByUsername(username)
                    .orElseThrow(() -> ex);
        }
    }

    private String getUsernameFromToken(JwtAuthenticationToken auth) {
        String preferred = auth.getToken().getClaimAsString("preferred_username");
        return (preferred != null && !preferred.isBlank()) ? preferred : auth.getName();
    }

    private Role extractRoleFromAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a.equals("EMPLOYEE") || a.equals("CUSTOMER"))
                .findFirst()
                .map(Role::valueOf)
                .orElse(Role.CUSTOMER);
    }
}
