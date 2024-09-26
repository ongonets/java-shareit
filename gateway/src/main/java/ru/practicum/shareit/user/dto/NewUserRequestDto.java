package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewUserRequestDto {
    @NotNull
    String name;
    @NotNull
    String email;
}
