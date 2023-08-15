package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfter(Long bookerId, LocalDateTime start, LocalDateTime end,
                                                              Pageable page);

    List<Booking> findByBookerIdAndEndIsBefore(Long bookerId, LocalDateTime end, Pageable page);

    List<Booking> findByBookerIdAndStatus(Long bookerId, Status status, Pageable page);

    Optional<Booking> findFirstByItemIdAndBookerIdAndEndIsBeforeAndStatus(Long itemId, Long userId,
                                                                 LocalDateTime end, Status status);

    Boolean existsByItemId(Long itemId);

    List<Booking> findByBookerId (Long userId, Pageable page);

    List<Booking> findByItemOwnerIdAndStatus(Long bookerId, Status status, Pageable page);

    List<Booking> findByItemOwnerIdOrderByIdDesc(Long ownerId, Pageable page);

    Optional<Booking> findFirstByItemIdAndStartBeforeOrderByStartDesc(Long itemId, LocalDateTime start);

    Optional<Booking> findFirstByItemIdAndStartAfterOrderByStartAsc(Long itemId, LocalDateTime start);

    List<Booking> findByItemOwnerIdAndStartIsBeforeAndEndIsAfter(Long ownerId, LocalDateTime start, LocalDateTime end,
                                                                 Pageable page);

    List<Booking> findByItemOwnerIdAndEndIsBefore(Long ownerId, LocalDateTime end, Pageable page);

    List<Booking> findByBookerIdAndStartIsAfter(Long bookerId, LocalDateTime start, Pageable page);

    List<Booking> findByItemOwnerIdAndStartIsAfter(Long ownerId, LocalDateTime start, Pageable page);
}