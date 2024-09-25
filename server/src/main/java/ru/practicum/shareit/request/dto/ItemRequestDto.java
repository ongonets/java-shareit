package ru.practicum.shareit.request.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequestDto {
    long id;
    String description;
    LocalDateTime created;
}
