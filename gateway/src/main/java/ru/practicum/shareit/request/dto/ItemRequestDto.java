package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemRequestDto {
    @NotNull(message = "Description was entered incorrectly")
    @NotBlank(message = "Description was entered incorrectly")
    String description;
}
