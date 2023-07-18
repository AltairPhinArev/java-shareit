package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryItemStorage implements ItemStorage {

    Long itemId = 1L;

    Map<Long, ItemDto> itemMap = new HashMap<>();

    UserService userService;
    UserMapper userMapper;
    ItemRequestService itemRequestService;
    ItemRequestMapper itemRequestMapper;

    @Autowired
    public InMemoryItemStorage(UserService userService, UserMapper userMapper, ItemRequestMapper itemRequestMapper,
                               ItemRequestService itemRequestService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.itemRequestMapper = itemRequestMapper;
        this.itemRequestService = itemRequestService;
    }

    @Override
    public Collection<ItemDto> getAllItems(Long userId) {
        if (userId == null) {
            return itemMap.values();
        } else {
            return itemMap.values().stream()
                    .filter(itemDto -> itemDto.getOwner().getId().equals(userId))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) {

        if (userMapper.toUser(userService.getUserById(userId)) != null) {
            itemDto.setOwner(userMapper.toUser(userService.getUserById(userId)));
        }
        validate(itemDto);
        itemMap.put(itemId, itemDto);
        itemDto.setId(itemId++);
        return itemDto;
    }

    @Override
    public ItemDto updateItem(ItemDto updatedItemDto, Long itemId, Long userId) {
        if (!itemMap.containsKey(itemId) ) {
            throw new NotFoundException("Item with id=" + itemId + " doesn't exist");
        }

        if (!Objects.equals(itemMap.get(itemId).getOwner().getId(), userId)) {
            throw new NotFoundException("Item with id=" + itemId + " doesn't exist");
        }
        ItemDto currentItemDto = itemMap.get(itemId);

        if (updatedItemDto.getName() != null) {
            currentItemDto.setName(updatedItemDto.getName());
        }
        if (updatedItemDto.getAvailable() != null) {
            currentItemDto.setAvailable(updatedItemDto.getAvailable());
        }
        if (updatedItemDto.getDescription() != null) {
            currentItemDto.setDescription(updatedItemDto.getDescription());
        }
        if (updatedItemDto.getOwner() != null) {
            currentItemDto.setOwner(userMapper.toUser(userService.getUserById(userId)));
        }

        validate(currentItemDto);
        itemMap.put(currentItemDto.getId(), currentItemDto);

        return currentItemDto;
    }

    @Override
    public void deleteItem(Long itemId) {
        if (itemMap.get(itemId) != null) {
            itemMap.remove(itemId);
        } else {
            throw new NotFoundException(HttpStatus.NOT_FOUND.toString());
        }
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        if (itemMap.get(itemId) != null) {
            return itemMap.get(itemId);
        } else {
            throw new NotFoundException(HttpStatus.NOT_FOUND.toString());
        }
    }

    @Override
    public List<ItemDto> getItemByDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            return Collections.emptyList(); // Return an empty list if the description is null or empty.
        }
        return itemMap.values().stream()
                .filter(itemDto -> (itemDto.getName().toLowerCase().contains(description.toLowerCase()) ||
                        itemDto.getDescription().toLowerCase().contains(description.toLowerCase())) &&
                        itemDto.getAvailable())
                .collect(Collectors.toList());
    }

    private ItemDto validate(ItemDto itemDto) {
        if (itemDto.getName() == null || itemDto.getName().isBlank()  || itemDto.getDescription() == null
                || itemDto.getOwner() == null || itemDto.getAvailable() == null) {
                throw new ValidationException("Illegal arguments for item");
        }
        return itemDto;
    }
}
