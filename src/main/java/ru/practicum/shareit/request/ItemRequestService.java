package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;

@Service
public class ItemRequestService {

    ItemRequestStorage itemRequestStorage;

    @Autowired
    public ItemRequestService(ItemRequestStorage itemRequestStorage) {
        this.itemRequestStorage = itemRequestStorage;
    }

    public Collection<ItemRequestDto> getAllItemRequest() {
        return  itemRequestStorage.getAllItemRequest();
    }

    public ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto) {
        return itemRequestStorage.createItemRequest(itemRequestDto);
    }

    public ItemRequestDto updateItemRequest(ItemRequestDto itemRequestDto) {
        return itemRequestStorage.updateItemRequest(itemRequestDto);
    }

    public ItemRequestDto getItemRequestById(Long itemRequestId) {
        return itemRequestStorage.getItemRequestById(itemRequestId);
    }

    public void deleteItemRequestById(Long itemRequestId) {
        itemRequestStorage.deleteItemRequestById(itemRequestId);
    }
}
