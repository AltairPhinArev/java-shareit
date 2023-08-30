package ru.practicum.shareit.negativeTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
public class NegativeBookingTests {

    @Mock
    BookingRepository bookingRepository;

    @Mock
    BookingRepository mockBookingRepository;

    @Mock
    ItemService itemService;

    @Mock
    UserService userService;


    @Mock
    BookingMapper bookingMapper;

    @Test
    public void shouldExceptionWhenGetBookingWithWrongId() {
        BookingService bookingService = new BookingServiceImpl(itemService, userService, bookingMapper, bookingRepository);
        when(userService.checkUser(any(Long.class)))
                .thenReturn(true);

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.getBookingById(-1L, 1L));
        Assertions.assertEquals("Booking with ID=-1 doesn't exist", exception.getMessage());
    }


}
