package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfter(Long bookerId, LocalDateTime start, LocalDateTime end);

    List<Booking> findByBookerIdAndEndIsBefore(Long bookerId, LocalDateTime end);

    List<Booking> findByBookerIdAndStatus(Long bookerId, Status status);

    Optional<Booking> findFirstByItemIdAndBookerIdAndEndIsBeforeAndStatus(Long itemId, Long userId,
                                                                 LocalDateTime end, Status status);
    Boolean existsByItemId(Long itemId);

    List<Booking> findByBookerId(Long userId);

    List<Booking> findByItemOwnerIdAndStatus(Long bookerId, Status status);

    List<Booking> findByItemOwnerId(Long ownerId);

    Booking findFirstByItemIdAndStartBeforeOrderByStartDesc(Long itemId, LocalDateTime start);
    Booking findFirstByItemIdAndStartAfterOrderByStartAsc(Long itemId, LocalDateTime start);

    List<Booking> findByItemOwnerIdAndStartIsBeforeAndEndIsAfter(Long ownerId, LocalDateTime start, LocalDateTime end);

    List<Booking> findByItemOwnerIdAndEndIsBefore(Long ownerId, LocalDateTime end);

    List<Booking> findByBookerIdAndStartIsAfter(Long bookerId, LocalDateTime start);

    List<Booking> findByItemOwnerIdAndStartIsAfter(Long ownerId, LocalDateTime start);
}