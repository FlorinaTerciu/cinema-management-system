package com.cinemasystem.entity;

import com.cinemasystem.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "family_name", nullable = false)
    private String familyName;

    @Column(name = "given_name", nullable = false)
    private String givenName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}
