package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDTO;
import java.util.List;

public interface BookingService {


    List<BookingDto> getAllBookingsByUserId(String status,Long userId);

    BookingDto createBooking(BookingInputDTO bookingInputDTO, Long userId);

    BookingDto updateBooking(Long bookingId, Long userId, Boolean approved);

    void deleteBooking(Long bookingId);

    List<BookingDto> getBookingsByOwner(String status, Long userId);

    BookingDto getBookingById(Long bookingId, Long userId);
}