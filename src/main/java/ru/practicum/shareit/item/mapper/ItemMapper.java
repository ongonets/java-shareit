package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.time.Instant;

@NoArgsConstructor
public final class ItemMapper {

        public static Item mapToItem(ItemDto itemDto) {
            Item item = new Item();
            item.setName(itemDto.getName());
            item.setDescription(itemDto.getDescription());
            item.setAvailable(itemDto.getAvailable());
            return item;
        }
}
