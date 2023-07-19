package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryItemRequestStorage implements ItemRequestStorage {

    Long itemRequestId = 1L;
    Map<Long, ItemRequestDto> itemRequestMap = new HashMap<>();

    ItemRequestMapper itemRequestMapper;

    UserService userService;

    UserMapper userMapper;

    @Autowired
    public InMemoryItemRequestStorage(UserMapper userMapper, ItemRequestMapper itemRequestMapper, UserService userService) {
        this.userMapper = userMapper;
        this.itemRequestMapper = itemRequestMapper;
        this.userService = userService;
    }

    @Override
    public Collection<ItemRequestDto> getAllItemRequest() {
        return itemRequestMap.values();
    }

    @Override
    public ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto) {
        itemRequestDto.setId(itemRequestId++);
        itemRequestDto.setCreated(LocalDateTime.now());
        itemRequestMap.put(itemRequestDto.getId(), itemRequestDto);
        log.info("itemRequest has been created {}", itemRequestDto.getId());
        return itemRequestDto;
    }

    @Override
    public ItemRequestDto updateItemRequest(ItemRequestDto itemRequestDto) {
        if (!itemRequestMap.containsKey(itemRequestDto.getId())) {
            throw new NotFoundException("item Request with id= " + itemRequestDto.getId() + "doesn't exist");
        }
        itemRequestMap.put(itemRequestDto.getId(), itemRequestDto);
        return itemRequestDto;
    }

    @Override
    public ItemRequestDto getItemRequestById(Long itemRequestId) {
       if (itemRequestMap.get(itemRequestId) != null) {
           return itemRequestMap.get(itemRequestId);
       } else {
            throw new NotFoundException("Cannot find itemRequest");
       }
    }

    @Override
    public void deleteItemRequestById(Long itemRequestId) {
        if (itemRequestMap.get(itemRequestId) != null) {
            itemRequestMap.remove(itemRequestId);
        } else {
            throw new NotFoundException("Cannot find itemRequest");
        }
    }
}
