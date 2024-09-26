package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewCommentRequestDto {
    @NotNull(message = "Text was entered incorrectly")
    @NotBlank(message = "Text was entered incorrectly")
    String text;
}