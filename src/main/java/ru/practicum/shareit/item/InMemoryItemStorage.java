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
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

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

        User user = userMapper.toUser(userService.getUserById(userId));

        if (user != null) {
            itemDto.setOwner(user);
        }

        validate(itemDto);
        itemDto.setId(itemId++);
        itemMap.put(itemDto.getId(), itemDto);
        log.info("itemRequest has been created {} by User with id= {}", itemDto.getId(), userId);

        return itemDto;
    }

    @Override
    public ItemDto updateItem(ItemDto updatedItemDto, Long itemId, Long userId) {
        if (!itemMap.containsKey(itemId)) {
            throw new NotFoundException("Item with id=" + itemId + " doesn't exist");
        }
        updatedItemDto.setId(itemId);
        if (!Objects.equals(itemMap.get(itemId).getOwner().getId(), userId)) {
            throw new NotFoundException("Item with id=" + itemId + " doesn't exist");
        }
        ItemDto currentItemDto = itemMap.get(itemId);

        if (updatedItemDto.getName() == null) {
            updatedItemDto.setName(currentItemDto.getName());
        }
        if (updatedItemDto.getAvailable() == null) {
            updatedItemDto.setAvailable(currentItemDto.getAvailable());
        }
        if (updatedItemDto.getDescription() == null) {
            updatedItemDto.setDescription(currentItemDto.getDescription());
        }
        if (updatedItemDto.getOwner() == null) {
            updatedItemDto.setOwner(userMapper.toUser(userService.getUserById(userId)));
        }

        validate(updatedItemDto);
        itemMap.put(updatedItemDto.getId(), updatedItemDto);
        log.info("item has been created {} by User with id= {}", updatedItemDto.getId(), userId);
        return updatedItemDto;
    }

    @Override
    public void deleteItem(Long itemId) {
        if (itemMap.get(itemId) != null) {
            itemMap.remove(itemId);
            log.info("item has been removed with id= {} ", itemId);
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
            return Collections.emptyList();
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
