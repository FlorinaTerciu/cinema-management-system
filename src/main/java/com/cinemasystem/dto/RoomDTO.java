package com.cinemasystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDTO {

    private Long id;

    @NotBlank(message = "Room name must not be blank")
    private String name;
}

