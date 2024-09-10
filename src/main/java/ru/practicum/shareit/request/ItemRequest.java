package ru.practicum.shareit.request;

/**
 * TODO Sprint add-item-requests.
 */

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequest {
    long id;
    long requesterId;
    String description;
    LocalDateTime created;
}
