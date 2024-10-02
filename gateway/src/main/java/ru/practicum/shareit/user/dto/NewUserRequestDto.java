package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewUserRequestDto {
    @NotNull(message = "Name was entered incorrectly")
    String name;
    @NotNull(message = "Email was entered incorrectly")
    String email;
}
