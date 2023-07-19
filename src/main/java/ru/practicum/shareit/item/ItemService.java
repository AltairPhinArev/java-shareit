package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;
import java.util.List;

@Service
public class ItemService {

    ItemStorage itemStorage;

    @Autowired
    public ItemService(ItemStorage itemStorage) {
        this.itemStorage = itemStorage;
    }

    public Collection<ItemDto> getAllItems(Long userId) {
        return itemStorage.getAllItems(userId);
    }

   public ItemDto createItem(ItemDto itemDto, Long userId) {
        return itemStorage.createItem(itemDto, userId);
    }

    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long userId) {
        return itemStorage.updateItem(itemDto, itemId, userId);
    }

    public void deleteItem(Long itemId) {
        itemStorage.deleteItem(itemId);
    }

    public ItemDto getItemById(Long itemId) {
        return itemStorage.getItemById(itemId);
    }

    public List<ItemDto> getItemByDescription(String description) {
        return itemStorage.getItemByDescription(description);
    }

}
