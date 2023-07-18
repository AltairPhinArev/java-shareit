package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;

public interface ItemRequestStorage {

    Collection<ItemRequestDto> getAllItemRequest();

    ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto);

    ItemRequestDto updateItemRequest(ItemRequestDto itemRequestDto);

    ItemRequestDto getItemRequestById(Long itemRequestId);

    void deleteItemRequestById(Long itemRequestId);
}
