package ru.practicum.shareit.request;

/**
 * TODO Sprint add-item-requests.
 */

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of = "id")
public class ItemRequest {
    long id;
    long requesterId;
    String description;
    LocalDateTime created;
}
