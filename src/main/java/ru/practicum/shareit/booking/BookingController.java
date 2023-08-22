package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDTO;


import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    BookingService bookingService;

    final static String USER_ID = "X-Sharer-User-Id";

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping(value = "/{bookingId}")
    public BookingDto getBookingById(@PathVariable Long bookingId, @RequestHeader(USER_ID) Long userId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getAllBookingByUserId(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                                  @RequestHeader(USER_ID) Long userId,
                                                  @RequestParam(defaultValue = "0") Integer from,
                                                  @RequestParam(defaultValue = "10") Integer size) {
        return bookingService.getAllBookingsByUserId(state, userId, from, size);
    }

    @GetMapping(value = "/owner")
    public List<BookingDto> getBookingsByOwner(
            @RequestParam(name = "state", defaultValue = "ALL") String state,
            @RequestHeader(USER_ID) Long userId,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        return bookingService.getBookingsByOwner(state, userId, from, size);
    }

    @ResponseBody
    @PostMapping
    public BookingDto createBooking(@RequestBody BookingInputDTO bookingInputDTO,
                                    @RequestHeader(USER_ID) Long userId) {
        return bookingService.createBooking(bookingInputDTO, userId);
    }

    @ResponseBody
    @PatchMapping(value = "/{bookingId}")
    public BookingDto updateBooking(@PathVariable Long bookingId,
                                    @RequestHeader(USER_ID) Long userId, @RequestParam Boolean approved) {
        return bookingService.updateBooking(bookingId, userId, approved);
    }
}
