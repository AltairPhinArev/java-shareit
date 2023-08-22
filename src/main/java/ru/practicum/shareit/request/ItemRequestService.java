package ru.practicum.shareit.request;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
public interface ItemRequestService {

    Collection<ItemRequestDto> getAllItemRequest(Long userId, Integer from, Integer size);

    ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, Long userId);

    ItemRequestDto getItemRequestById(Long itemRequestId, Long userId);

    List<ItemRequestDto> getOwnItemRequests(Long userId, Integer from, Integer size);
}
