package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.Collection;

public interface BookingStorage {

    Collection<BookingDto> getAllBooking();

    BookingDto createBooking(BookingDto bookingDto);

    BookingDto updateBooking(BookingDto bookingDto);

    void deleteBooking(Long bookingId);

    BookingDto getBookingById(Long bookingId);
}
