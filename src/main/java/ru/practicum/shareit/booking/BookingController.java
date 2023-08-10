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

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping(value = "/{bookingId}")
    public BookingDto getBookingById(@PathVariable Long bookingId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getAllBookingByUserId(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                                  @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getAllBookingsByUserId(state, userId);
    }

    @GetMapping(value = "/owner")
    public List<BookingDto> getBookingsByOwner(
            @RequestParam(name = "state", defaultValue = "ALL") String state,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getBookingsByOwner(state, userId);
    }

    @ResponseBody
    @PostMapping
    public BookingDto createBooking(@RequestBody BookingInputDTO bookingInputDTO,
                                    @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.createBooking(bookingInputDTO, userId);
    }

    @ResponseBody
    @PatchMapping(value = "/{bookingId}")
    public BookingDto updateBooking(@PathVariable Long bookingId,
                                    @RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam Boolean approved) {
        return bookingService.updateBooking(bookingId, userId, approved);
    }

    @DeleteMapping
    public void deleteBookingById(@PathVariable Long bookingId) {
        bookingService.deleteBooking(bookingId);
    }
}
