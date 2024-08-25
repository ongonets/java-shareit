package ru.practicum.shareit.item.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.model.Item;


@NoArgsConstructor
public final class ItemMapper {

        public static Item mapToItem(NewItemRequest newItemRequest) {
            Item item = new Item();
            item.setName(newItemRequest.getName());
            item.setDescription(newItemRequest.getDescription());
            item.setAvailable(newItemRequest.getAvailable());
            return item;
        }
}
