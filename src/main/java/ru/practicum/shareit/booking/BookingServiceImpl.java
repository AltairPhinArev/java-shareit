package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDTO;
import ru.practicum.shareit.booking.dto.ShortBookingDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

    ItemService itemService;

    UserService userService;

    BookingRepository bookingRepository;

    BookingMapper bookingMapper;

    @Autowired
    @Lazy
    public BookingServiceImpl(ItemService itemService, UserService userService, BookingMapper bookingMapper,
                              BookingRepository bookingRepository) {
        this.itemService = itemService;
        this.userService = userService;
        this.bookingMapper = bookingMapper;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public BookingDto createBooking(BookingInputDTO bookingInputDTO, Long userId) {
        validate(bookingInputDTO, userId);
        return BookingMapper.toBookingDto(
                bookingRepository.save(BookingMapper.toBookingFromGson(bookingInputDTO,
                        ItemMapper.toItem(itemService.getItem(bookingInputDTO.getItemId())),
                        UserMapper.toUser(userService.getUserById(userId)))));
    }

    @Override
    public BookingDto updateBooking(Long bookingId, Long userId, Boolean approved) {
        userService.checkUser(userId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with ID=" + bookingId + " doesn't exist"));

        if (booking.getEnd().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Time of bookings is Up");
        }

        if (booking.getBooker().getId().equals(userId)) {
            if (!approved) {
                booking.setStatus(Status.CANCELED);
                log.info("User with ID={} CANCELED booking with ID={}", userId, bookingId);
            } else {
                throw new NotFoundException("Only the owner of the item can confirm the booking");
            }

        } else if ((Objects.equals(itemService.getItem(booking.getItem().getId()).getOwner().getId(), userId)) &&
                (!booking.getStatus().equals(Status.CANCELED))) {

            if (!booking.getStatus().equals(Status.WAITING)) {
                throw new ValidationException("The booking decision has already been made");
            }

            if (approved) {
                booking.setStatus(Status.APPROVED);
                log.info("User with ID={} APPROVED booking with ID={}", userId, bookingId);

            } else {
                booking.setStatus(Status.REJECTED);
                log.info("User with ID={} REJECTED booking with ID={}", userId, bookingId);
            }

        } else {
            if (booking.getStatus().equals(Status.CANCELED)) {
                throw new ValidationException("The booking has been CANCELED");
            } else {
                throw new ValidationException("Only the owner of the item can confirm the booking");
            }
        }

        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBookingByItemIdAndUserId(Long itemId, Long userId) {
        Optional<Booking> bookingOptional = bookingRepository.findFirstByItemIdAndBookerIdAndEndIsBeforeAndStatus(
                itemId, userId, LocalDateTime.now(), Status.APPROVED);
        return bookingOptional.map(BookingMapper::toBookingDto).orElse(null);
    }


    @Override
    public List<BookingDto> getAllBookingsByUserId(String state, Long userId, Integer from, Integer size) {
        userService.checkUser(userId);
        if (from < 0 || size <= 0) {
            throw new ValidationException("Illegal params");
        }

        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size, Sort.by("start").descending());

        List<Booking> bookings;

        switch (state) {
            case "ALL":
                bookings = bookingRepository.findByBookerId(userId, page);
                break;
            case "CURRENT":
                bookings = bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfter(userId, LocalDateTime.now(),
                        LocalDateTime.now(), page);
                break;
            case "PAST":
                bookings = bookingRepository.findByBookerIdAndEndIsBefore(userId, LocalDateTime.now(), page);
                break;
            case "FUTURE":
                bookings = bookingRepository.findByBookerIdAndStartIsAfter(userId, LocalDateTime.now(), page);
                break;
            case "WAITING":
                bookings = bookingRepository.findByBookerIdAndStatus(userId, Status.WAITING, page);
                break;
            case "REJECTED":
                bookings = bookingRepository.findByBookerIdAndStatus(userId, Status.REJECTED, page);
                break;
            default:
                throw new ValidationException("Unknown state: " + state);
        }

        bookings.sort(Comparator.comparing(Booking::getStart).reversed());

        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getBookingsByOwner(String status, Long userId, Integer from, Integer size) {
        userService.checkUser(userId);
        if (from < 0 || size <= 0) {
            throw new ValidationException("Illegal params");
        }
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);

        List<BookingDto> bookingDtos;
        switch (status) {
            case "ALL":
                bookingDtos = bookingRepository.findByItemOwnerIdOrderByIdDesc(userId, page).stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
                break;
            case "CURRENT":
                bookingDtos = bookingRepository.findByItemOwnerIdAndStartIsBeforeAndEndIsAfter(userId, LocalDateTime.now(),
                                LocalDateTime.now(), page).stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
                Collections.reverse(bookingDtos);
                break;
            case "PAST":
                bookingDtos = bookingRepository.findByItemOwnerIdAndEndIsBefore(userId, LocalDateTime.now(), page).stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
                Collections.reverse(bookingDtos);
                break;
            case "FUTURE":
                bookingDtos = bookingRepository.findByItemOwnerIdAndStartIsAfter(userId, LocalDateTime.now(), page).stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
                Collections.reverse(bookingDtos);
                break;
            case "WAITING":
                bookingDtos = bookingRepository.findByItemOwnerIdAndStatus(userId, Status.WAITING, page).stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
                Collections.reverse(bookingDtos);
                break;
            case "REJECTED":
                bookingDtos = bookingRepository.findByItemOwnerIdAndStatus(userId, Status.REJECTED, page).stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
                Collections.reverse(bookingDtos);
                break;
            default:
                throw new ValidationException("Unknown state: " + status);
        }


        return bookingDtos;
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        userService.checkUser(userId);

        BookingDto bookingDto = BookingMapper.toBookingDto(bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with ID=" + bookingId + " doesn't exist")));

        if (bookingDto.getBooker().getId().equals(userId) || bookingDto.getItem().getOwner().getId().equals(userId)) {
            return bookingDto;
        } else {
            throw new NotFoundException(String.valueOf(bookingDto));
        }
    }

    private void validate(BookingInputDTO bookingInputDTO, Long userId) {
        if (!userService.checkUser(userId) || itemService.getItem(bookingInputDTO.getItemId()) == null) {
            throw new NotFoundException("Cannot find user with id= " + userId);
        }
        if (Objects.equals(itemService.getItem(bookingInputDTO.getItemId()).getOwner().getId(), userId)) {
            throw new NotFoundException("You cannot book your own item.");
        }

        if (bookingInputDTO.getStart() == null || bookingInputDTO.getEnd() == null ||
                bookingInputDTO.getStart().isEqual(bookingInputDTO.getEnd()) ||
                bookingInputDTO.getStart().isAfter(bookingInputDTO.getEnd()) ||
                bookingInputDTO.getStart().isBefore(LocalDateTime.now()) ||
                !itemService.getItem(bookingInputDTO.getItemId()).getAvailable()) {
            throw new ValidationException("Date of start must be earlier than end");
        }
    }

    @Override
    public ShortBookingDto getLastBooking(Long itemId) {
        Booking lastBooking = bookingRepository.findFirstByItemIdAndStartBeforeOrderByStartDesc(itemId,
                LocalDateTime.now()).orElse(null);
        if (lastBooking != null) {
            if (lastBooking.getStatus() != Status.REJECTED) {
                return BookingMapper.toShortBookingDto(lastBooking);
            }
        }
        return null;
    }

    @Override
    public ShortBookingDto getNextBooking(Long itemId) {
        Booking nextBooking = bookingRepository.findFirstByItemIdAndStartAfterOrderByStartAsc(itemId,
                LocalDateTime.now()).orElse(null);
        if (nextBooking != null) {
            if (nextBooking.getStatus() != Status.REJECTED) {
                return BookingMapper.toShortBookingDto(nextBooking);
            }
        }
        return null;
    }
}