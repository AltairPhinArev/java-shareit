package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.ShortBookingDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.InputCommentDto;
import ru.practicum.shareit.item.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoFull;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    ItemRepository itemRepository;
    UserService userService;
    CommentRepository commentRepository;

    BookingRepository bookingRepository;


    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserService userService,
                           CommentRepository commentRepository,
                           BookingRepository bookingRepository) {
        this.itemRepository = itemRepository;
        this.userService = userService;
        this.commentRepository = commentRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public CommentDto createNewComment(InputCommentDto inputCommentDto, Long userId, Long itemId) {

        Booking booking = bookingRepository.findFirstByItemIdAndBookerIdAndEndIsBeforeAndStatus(itemId, userId,
                LocalDateTime.now(), Status.APPROVED);

        if (booking == null) {
            throw new ValidationException(":* (");
        }

        if (bookingRepository.existsByItemId(itemId)) {

            userService.checkUser(userId);
            checkItem(itemId);
            ItemDto item = getItem(itemId);
            validate(inputCommentDto, item);

            return CommentMapper.toCommentDto(commentRepository.save(new Comment(
                    null,
                    inputCommentDto.getText(),
                    ItemMapper.toItem(getItem(itemId)),
                    UserMapper.toUser(userService.getUserById(userId)),
                    LocalDateTime.now())
            ));
        } else {
            throw new ValidationException(":* (");
        }
    }

    @Override
    public List<CommentDto> getCommentByUserId(Long userId) {
        return commentRepository.findByAuthorId(userId).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemDtoFull> getAllItems(Long userId) {
        List<Item> items = itemRepository.findByOwnerId(userId);

        List<ItemDtoFull> itemDtoFulls = new ArrayList<>();

        for (Item item : items) {

            ShortBookingDto nextBooking = null;
            ShortBookingDto lastBooking = null;

            List<CommentDto> comments = new ArrayList<>();
            if (commentRepository.existsByItemId(item.getId())) {
                comments.addAll(commentRepository.findByItemId(item.getId()).stream()
                        .map(CommentMapper::toCommentDto)
                        .collect(Collectors.toList()));
            }

            if (bookingRepository.existsByItemId(item.getId())) {

                try {
                    lastBooking = getLastBooking(item.getId());
                } catch (NullPointerException e) {
                    lastBooking = null;
                }

                try {
                    nextBooking = getNextBooking(item.getId());
                } catch (NullPointerException e) {
                    nextBooking = null;
                }
            }


            itemDtoFulls.add(new ItemDtoFull(
                    item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.getAvailable(),
                    item.getOwner(),
                    lastBooking,
                    nextBooking,
                    comments
            ));
        }

        return itemDtoFulls;
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) {
        try {
            validate(itemDto, userId);
            User owner = UserMapper.toUser(userService.getUserById(userId));
            itemDto.setOwner(owner);

            return ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(itemDto)));
        } catch (ValidationException e) {
            throw e;
        }
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long userId) {
        if (itemDto.getId() == null) {
            itemDto.setId(userId);
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("User with ID=" + userId + " doesn't exist"));
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        if (itemDto.getOwner() != null) {
            item.setOwner(UserMapper.toUser(userService.getUserById(itemDto.getOwner().getId())));
        }

        validate(ItemMapper.toItemDto(item), userId);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public void deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);
    }

    @Override
    public ItemDto getItem(Long itemId) {
        return ItemMapper.toItemDto(itemRepository.findById(itemId).orElseThrow(()
                -> new NotFoundException("Item with ID=" + itemId + " doesn't exist")));
    }

    @Override
    public ItemDtoFull getItemById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(()
                -> new NotFoundException("Item with ID=" + itemId + " doesn't exist"));

        ShortBookingDto nextBooking = null;
        ShortBookingDto lastBooking = null;

        List<CommentDto> comments = new ArrayList<>();

        if (userId.equals(item.getOwner().getId())) {

            if (bookingRepository.existsByItemId(itemId)) {

                try {
                    lastBooking = getLastBooking(itemId);
                } catch (NullPointerException e) {
                    lastBooking = null;
                }

                try {
                    nextBooking = getNextBooking(itemId);
                } catch (NullPointerException e) {
                    nextBooking = null;
                }

            }
        }

        if (commentRepository.existsByItemId(itemId)) {
            comments.addAll(commentRepository.findByItemId(itemId).stream()
                    .map(CommentMapper::toCommentDto)
                    .collect(Collectors.toList()));
        }


        return new ItemDtoFull(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                lastBooking,
                nextBooking,
                comments
        );
    }


    @Override
    public List<ItemDto> getItemByDescription(String description) {
        if (description == null || description.isBlank()) {
            return Collections.emptyList();
        } else {
            return itemRepository.search(description).stream()
                    .filter(Item::getAvailable)
                    .map(ItemMapper::toItemDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public boolean checkItem(Long itemId) {
        if (itemRepository.existsById(itemId)) {
            return true;
        } else {
            throw new NotFoundException("Can't find item with ID= " + itemId);
        }
    }

    private ItemDto validate(ItemDto itemDto, Long userId) {
        if (itemDto.getName() == null || itemDto.getName().isBlank() || itemDto.getDescription() == null
                || itemDto.getAvailable() == null || userService.getUserById(userId) == null) {
            throw new ValidationException("Illegal arguments for item");
        } else {
            return itemDto;
        }
    }

    private void validate(InputCommentDto inputCommentDto, ItemDto itemDto) {
        if (inputCommentDto.getText() == null || inputCommentDto.getText().isBlank()) {
            throw new ValidationException("Illegal factor for comment");
        }
    }


    private ShortBookingDto getLastBooking(Long itemId) {
        Booking lastBooking = bookingRepository.findFirstByItemIdAndStartBeforeOrderByStartDesc(itemId,
                LocalDateTime.now());
        if (lastBooking.getStatus() != Status.REJECTED) {
            return BookingMapper.toShortBookingDto(lastBooking);
        } else {
            return null;
        }
    }

    private ShortBookingDto getNextBooking(Long itemId) {
        Booking nextBooking = bookingRepository.findFirstByItemIdAndStartAfterOrderByStartAsc(itemId,
                LocalDateTime.now());
        if (nextBooking.getStatus() != Status.REJECTED) {
            return BookingMapper.toShortBookingDto(nextBooking);
        } else {
            return null;
        }
    }
}