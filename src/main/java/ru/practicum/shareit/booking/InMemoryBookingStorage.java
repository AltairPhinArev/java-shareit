package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.Collection;

@Component
public class InMemoryBookingStorage implements BookingStorage {
    @Override
    public Collection<BookingDto> getAllBooking() {
        return null;
    }

    @Override
    public BookingDto createBooking(BookingDto bookingDto) {
        return null;
    }

    @Override
    public BookingDto updateBooking(BookingDto bookingDto) {
        return null;
    }

    @Override
    public void deleteBooking(Long bookingId) {

    }

    @Override
    public BookingDto getBookingById(Long bookingId) {
        return null;
    }
}
