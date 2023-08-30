package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingInputDTO {

    private Long itemId;

    private LocalDateTime start;

    private LocalDateTime end;

    @Builder
    public BookingInputDTO(Long itemId, LocalDateTime start, LocalDateTime end) {
        this.itemId = itemId;
        this.start = start;
        this.end = end;
    }
}
