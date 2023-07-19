package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    BookingStorage bookingStorage;

    @Autowired
    public BookingService(BookingStorage bookingStorage) {
        this.bookingStorage = bookingStorage;
    }
}
