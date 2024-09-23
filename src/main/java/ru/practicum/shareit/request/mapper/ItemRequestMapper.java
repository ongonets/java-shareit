package ru.practicum.shareit.request.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ShortItemDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItem;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
public class ItemRequestMapper {
    public static ItemRequestDto mapToDto(ItemRequest itemRequest) {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setId(itemRequest.getId());
        requestDto.setDescription(itemRequest.getDescription());
        requestDto.setCreated(itemRequest.getCreated());
        return requestDto;
    }

    public static ItemRequest mapToItemRequest(ItemRequestDto itemRequestDto, User user){
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setRequester(user);
        itemRequest.setCreated(LocalDateTime.now());
        return itemRequest;
    }

    public static ItemRequestWithItem mapToDto(ItemRequest itemRequest, List<ShortItemDto> items) {
        ItemRequestWithItem requestDto = new ItemRequestWithItem();
        requestDto.setId(itemRequest.getId());
        requestDto.setDescription(itemRequest.getDescription());
        requestDto.setCreated(itemRequest.getCreated());
        requestDto.setItems(items);
        return requestDto;
    }

}
