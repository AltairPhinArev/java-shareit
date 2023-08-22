package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemRequestDto {

     Long id;

     String description;

     User requester;

     LocalDateTime created;

     List<ItemDto> items;

     @Builder
     public ItemRequestDto(Long id, String description, User requester, LocalDateTime created, List<ItemDto> items) {
          this.id = id;
          this.description = description;
          this.requester = requester;
          this.created = created;
          this.items = items;
     }
}
