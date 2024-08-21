package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TODO Sprint add-controllers.
 */
@Data
@EqualsAndHashCode(of = "id")
public class Item {
    long id;
    long ownerId;
    String name;
    String description;
    boolean available;
}
