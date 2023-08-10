package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.ShortBookingDto;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Data
public class ItemDtoFull {

    Long id;

    String name;

    String description;

    Boolean available;

    User owner;

    ShortBookingDto lastBooking;

    ShortBookingDto nextBooking;

    List<CommentDto> comments;

    @Builder
    public ItemDtoFull(Long id, String name, String description, Boolean available,
                       User owner, ShortBookingDto lastBooking,
                       ShortBookingDto nextBooking, List<CommentDto> comments) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.lastBooking = lastBooking;
        this.nextBooking = nextBooking;
        this.comments = comments;
    }
}
