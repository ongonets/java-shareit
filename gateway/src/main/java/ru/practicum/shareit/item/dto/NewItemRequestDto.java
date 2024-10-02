package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class NewItemRequestDto {
    @NotNull(message = "Name was entered incorrectly")
    @NotBlank(message = "Name was entered incorrectly")
    String name;
    @NotNull(message = "Description was entered incorrectly")
    @NotBlank(message = "Description was entered incorrectly")
    String description;
    @NotNull(message = "Available was entered incorrectly")
    Boolean available;
    Long requestId;
}
