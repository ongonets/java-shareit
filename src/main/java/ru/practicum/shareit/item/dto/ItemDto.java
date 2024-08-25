package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class ItemDto {
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
