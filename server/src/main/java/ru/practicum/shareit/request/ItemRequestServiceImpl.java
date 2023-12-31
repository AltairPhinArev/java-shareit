package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {

    ItemRequestRepository itemRequestRepository;

    UserService userService;

    ItemService itemService;

    @Lazy
    @Autowired
    public ItemRequestServiceImpl(UserService userService, ItemService itemService,
                                  ItemRequestRepository itemRequestRepository) {
        this.userService = userService;
        this.itemService = itemService;
        this.itemRequestRepository = itemRequestRepository;
    }

    @Override
    public Collection<ItemRequestDto> getAllItemRequest(Long userId, Integer from, Integer size) {
        userService.checkUser(userId);

        if (from < 0 || size < 0) {
            throw new ValidationException("Illegal params");
        }

        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        return itemRequestRepository.findAllByRequesterIdNotOrderByCreatedDesc(userId, page).stream()
                .map(itemRequest -> ItemRequestMapper.toItemRequestDto(itemRequest,
                        itemService.getItemByRequestId(itemRequest.getId())))
                .sorted(Comparator.comparing(ItemRequestDto::getCreated))
                .collect(toList());
    }

    @Override
    public ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, Long userId) {
        if (itemRequestDto.getDescription() == null || itemRequestDto.getDescription().isBlank()) {
            throw new ValidationException("Description cannot be null or blank");
        }
        itemRequestDto.setCreated(LocalDateTime.now());
        ItemRequest itemRequest = ItemRequestMapper.toCreatedItemRequest(itemRequestDto,
                UserMapper.toUser(userService.getUserById(userId)), LocalDateTime.now());
        log.info("ItemRequest was created by user = {}", userId);
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest),
                itemService.getItemByRequestId(itemRequestDto.getId()));
    }

    @Override
    public ItemRequestDto getItemRequestById(Long itemRequestId, Long userId) {
        userService.checkUser(userId);
        ItemRequest itemRequest = itemRequestRepository.findById(itemRequestId)
                .orElseThrow(() -> new NotFoundException("request with ID=" + itemRequestId + " doesn't exist"));
        return ItemRequestMapper.toItemRequestDto(itemRequest, itemService.getItemByRequestId(itemRequestId));
    }

    @Override
    public List<ItemRequestDto> getOwnItemRequests(Long userId, Integer from, Integer size) {
        userService.checkUser(userId);

        if (from < 0 || size < 0) {
            throw new ValidationException("Illegal params");
        }

        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        return itemRequestRepository.findAllByRequesterId(userId, page).stream()
                .map(itemRequest -> ItemRequestMapper.toItemRequestDto(itemRequest,
                        itemService.getItemByRequestId(itemRequest.getId())))
                .sorted(Comparator.comparing(ItemRequestDto::getCreated))
                .collect(toList());
    }
}