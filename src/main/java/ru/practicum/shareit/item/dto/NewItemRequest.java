package ru.practicum.shareit.item.dto;

import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class NewItemRequest {
    String name;
    String description;
    Boolean available;

    public boolean hasName() {
        return getName() != null;
    }

    public boolean hasDescription() {
        return getDescription() != null;
    }

    public boolean hasAvailable() {
        return getAvailable() != null;
    }
}
