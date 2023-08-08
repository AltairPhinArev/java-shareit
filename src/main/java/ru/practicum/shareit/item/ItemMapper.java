package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoFull;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemMapper {

    BookingService bookingService;


    @Autowired
    public ItemMapper(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner() != null ? item.getOwner() : null
        );
    }

    public static ItemDto toItemDtoFromFullItemDto(ItemDtoFull item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner() != null ? item.getOwner() : null
        );
    }

    public static Item toItem(ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                itemDto.getOwner()
        );
    }

    public static Item toItem(ItemDtoFull itemDtoFull) {
        return new Item(
                itemDtoFull.getId(),
                itemDtoFull.getName(),
                itemDtoFull.getDescription(),
                itemDtoFull.getAvailable(),
                itemDtoFull.getOwner()
        );
    }
}