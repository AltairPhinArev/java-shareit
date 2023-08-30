package ru.practicum.shareit.booking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShortBookingDto {

    private Long id;

    private Long bookerId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    public ShortBookingDto(Long id, Long bookerId, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.bookerId = bookerId;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
