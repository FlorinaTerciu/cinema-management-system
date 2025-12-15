package com.cinemasystem.dto;

import com.cinemasystem.entity.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;

    @NotBlank(message = "Username must not be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Family name must not be blank")
    @Size(min = 1, max = 100)
    private String familyName;

    @NotBlank(message = "Given name must not be blank")
    @Size(min = 1, max = 100)
    private String givenName;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be a valid email address")
    private String email;

    private LocalDateTime createdAt;

    private Role role;
}
