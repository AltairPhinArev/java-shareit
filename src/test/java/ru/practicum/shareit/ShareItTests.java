package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;

import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDTO;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.RegisterException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.InputCommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoFull;
import ru.practicum.shareit.request.ItemRequestImpl;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureCache
class ShareItTests {

    UserService userService;

    ItemService itemService;

    ItemMapper itemMapper;

    ItemRepository itemRepository;

    UserRepository userRepository;

    BookingRepository bookingRepository;

    CommentRepository commentRepository;

    ItemRequestRepository itemRequestRepository;

    BookingService bookingService;

    ItemRequestService itemRequestService;

    BookingMapper bookingMapper;

    @Autowired
    public ShareItTests(UserService userService, ItemService itemService, ItemMapper itemMapper,
                        ItemRepository itemRepository, UserRepository userRepository,
                        BookingRepository bookingRepository, ItemRequestRepository itemRequestRepository,
                        BookingService bookingService, ItemRequestService itemRequestService,
                        BookingMapper bookingMapper, CommentRepository commentRepository) {
        this.userService = userService;
        this.itemService = itemService;
        this.itemMapper = itemMapper;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.itemRequestRepository = itemRequestRepository;
        this.bookingService = bookingService;
        this.itemRequestService = itemRequestService;
        this.bookingMapper = bookingMapper;
        this.commentRepository = commentRepository;
    }

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(userRepository);
        itemService = new ItemServiceImpl(itemRepository, userService,
                commentRepository, bookingRepository, bookingService, itemRequestService);
        bookingService = new BookingServiceImpl(itemService, userService, bookingMapper, bookingRepository);
        itemRequestService = new ItemRequestImpl(userService, itemService, itemRequestRepository);
        itemService = new ItemServiceImpl(itemRepository, userService,
                commentRepository, bookingRepository, bookingService, itemRequestService);
    }

    @Test
    void contextLoads() {
    }

    @Test
    public void testUserCreate() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("USER")
                .email("user11@example.com")
                .build();

        assertEquals(userDto, userService.createUser(userDto));
    }

    @Test
    public void testUserUpdate() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("USER")
                .email("emaily@gmail.con")
                .build();

        userService.createUser(userDto);
        userDto.setName("Anton");
        assertEquals(userDto, userService.updateUser(userDto, userDto.getId()));
    }

    @Test
    public void testUserGetId() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("USER")
                .email("user16@example.com")
                .build();

        userService.createUser(userDto);
        assertEquals(userDto, userService.getUserById(userDto.getId()));
    }


    @Test
    public void testItemCreate() {
        UserDto useDto = UserDto.builder()
                .id(1L)
                .name("USER")
                .email("user18@example.com")
                .build();

        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Disc")
                .owner(UserMapper.toUser(useDto))
                .available(true)
                .description("Disc")
                .build();
        userService.createUser(useDto);
        itemService.createItem(itemDto, useDto.getId());

        assertEquals(useDto, userService.getUserById(useDto.getId()));

        assertEquals(itemDto.getId(), itemService.getItemById(itemDto.getId(), useDto.getId()).getId());

        assertEquals(itemDto.getDescription(),
                itemService.getItemById(itemDto.getId(), useDto.getId()).getDescription());

        assertEquals(itemDto.getAvailable(),
                itemService.getItemById(itemDto.getId(), useDto.getId()).getAvailable());

        assertEquals(itemDto.getOwner(),
                itemService.getItemById(itemDto.getId(), useDto.getId()).getOwner());
    }

    @Test
    public void testItemUpdate() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("USER")
                .email("user19@example.com")
                .build();

        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Disc")
                .owner(UserMapper.toUser(userDto))
                .available(true)
                .description("Disc")
                .build();

        userService.createUser(userDto);
        itemService.createItem(itemDto, userDto.getId());

        itemDto.setDescription("Descriptor");

        assertEquals(itemDto, itemService.updateItem(itemDto, itemDto.getId(), userDto.getId()));
    }

    @Test
    public void testItemGetById() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("USER")
                .email("yaml@mail.com")
                .build();

        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Disc")
                .owner(UserMapper.toUser(userDto))
                .available(true)
                .description("Disc")
                .build();

        userService.createUser(userDto);
        itemService.createItem(itemDto, userDto.getId());

        assertEquals(userDto, userService.getUserById(userDto.getId()));
        assertEquals(itemDto.getId(), itemService.getItemById(itemDto.getId(), userDto.getId()).getId());
        assertEquals(itemDto.getDescription(), itemService.getItemById(itemDto.getId(), userDto.getId()).getDescription());
        assertEquals(itemDto.getAvailable(), itemService.getItemById(itemDto.getId(), userDto.getId()).getAvailable());
        assertEquals(itemDto.getOwner(), itemService.getItemById(itemDto.getId(), userDto.getId()).getOwner());
    }

    @Test
    public void testItemGetByText() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("USER")
                .email("valid@email.com")
                .build();

        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Comp")
                .owner(UserMapper.toUser(userDto))
                .available(true)
                .description("Comp")
                .build();

        userService.createUser(userDto);
        itemService.createItem(itemDto, userDto.getId());
        List<ItemDto> itemDtos = new ArrayList<>();

        itemDtos.add(ItemMapper.toItemDtoFromFullItemDto(itemService.getItemById(itemDto.getId(), userDto.getId())));

        assertEquals(userDto, userService.getUserById(userDto.getId()));
        assertEquals(itemDtos, itemService.getItemByDescription(itemDto.getName(), 0, 10));
    }

    @Test
    public void testUserMapper() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("USER")
                .email("yaml@.com")
                .build();

        User user = User.builder()
                .id(1L)
                .name("USER")
                .email("yaml@.com")
                .build();

        assertEquals(UserMapper.toUser(userDto), user);
    }

    @Test
    public void testCreateBookingAndUpdate() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("USER")
                .email("yaml@exmdt.com")
                .build();
        userService.createUser(userDto);

        UserDto userDto1 = UserDto.builder()
                .id(2L)
                .name("USER")
                .email("yampl@exmdt.com")
                .build();
        userService.createUser(userDto1);

        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Comp")
                .owner(UserMapper.toUser(userDto))
                .available(true)
                .description("Comp")
                .build();
        itemService.createItem(itemDto, userDto.getId());

        BookingInputDTO bookingInputDTO = BookingInputDTO.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2023, 11, 10, 11, 11))
                .end(LocalDateTime.of(2024, 11, 11, 12, 51))
                .build();

        BookingDto bookingDto = bookingService.createBooking(bookingInputDTO, userDto1.getId());

        BookingDto bookingDto1 = BookingDto.builder()
                .id(bookingDto.getId())
                .start(LocalDateTime.of(2023, 11, 10, 11, 11))
                .end(LocalDateTime.of(2024, 11, 11, 12, 51))
                .item(ItemMapper.toItem(itemDto))
                .booker(UserMapper.toUser(userDto1))
                .status(Status.WAITING)
                .build();

        assertEquals(bookingDto1, bookingDto);

        BookingDto bookingDto2 = bookingService.updateBooking(bookingDto.getId(), userDto.getId(), true);

        assertEquals(Status.APPROVED, bookingDto2.getStatus());
    }

    @Test
    public void getBookingById() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("USER")
                .email("yaml@exmdt.com")
                .build();
        userService.createUser(userDto);

        UserDto userDto1 = UserDto.builder()
                .id(2L)
                .name("USER")
                .email("yampl@exmdt.com")
                .build();
        userService.createUser(userDto1);

        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Comp")
                .owner(UserMapper.toUser(userDto))
                .available(true)
                .description("Comp")
                .build();
        itemService.createItem(itemDto, userDto.getId());

        BookingInputDTO bookingInputDTO = BookingInputDTO.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2023, 11, 10, 11, 11))
                .end(LocalDateTime.of(2024, 11, 11, 12, 51))
                .build();

        BookingDto bookingDto = bookingService.createBooking(bookingInputDTO, userDto1.getId());
        BookingDto booking = bookingService.getBookingById(bookingDto.getId(), userDto1.getId());

        Assertions.assertEquals(bookingDto.getId(), BookingMapper.toBooking(booking).getId());
        Assertions.assertEquals(bookingDto.getStart(), BookingMapper.toBooking(booking).getStart());
        Assertions.assertEquals(bookingDto.getEnd(), BookingMapper.toBooking(booking).getEnd());
        Assertions.assertEquals(bookingDto.getStatus(), BookingMapper.toBooking(booking).getStatus());
        Assertions.assertEquals(bookingDto.getItem(), BookingMapper.toBooking(booking).getItem());
    }

    @Test
    public void testGetBookingWrongId() {
        UserDto userDto1 = UserDto.builder()
                .id(1L)
                .name("USER")
                .email("yampl23234@exmdt.com")
                .build();

        userService.createUser(userDto1);

        UserDto userDto = UserDto.builder()
                .id(2L)
                .name("USER")
                .email("yaml@exmdt.com")
                .build();
        userService.createUser(userDto);

        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Comp")
                .owner(UserMapper.toUser(userDto))
                .available(true)
                .description("Comp")
                .build();
        itemService.createItem(itemDto, userDto.getId());

        BookingInputDTO bookingInputDTO = BookingInputDTO.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2023, 11, 10, 11, 11))
                .end(LocalDateTime.of(2024, 11, 11, 12, 51))
                .build();

        BookingDto bookingDto = bookingService.createBooking(bookingInputDTO, userDto1.getId());

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.getBookingById(100L, userDto.getId()));
        Assertions.assertEquals("Booking with ID=100 doesn't exist",
                exception.getMessage());
    }

    @Test
    public void testGetBookingWrongUserId() {
        UserDto userDto1 = UserDto.builder()
                .id(1L)
                .name("USER")
                .email("yampl23234@exmdt.com")
                .build();

        userService.createUser(userDto1);

        UserDto userDto = UserDto.builder()
                .id(2L)
                .name("USER")
                .email("yaml@exmdt.com")
                .build();
        userService.createUser(userDto);

        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Comp")
                .owner(UserMapper.toUser(userDto))
                .available(true)
                .description("Comp")
                .build();
        itemService.createItem(itemDto, userDto.getId());

        BookingInputDTO bookingInputDTO = BookingInputDTO.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2023, 11, 10, 11, 11))
                .end(LocalDateTime.of(2024, 11, 11, 12, 51))
                .build();

        BookingDto bookingDto = bookingService.createBooking(bookingInputDTO, userDto1.getId());

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.getBookingById(bookingDto.getId(), 100L));
        Assertions.assertEquals("User with ID= 100 doesn't exist",
                exception.getMessage());
    }


    @Test
    public void testCreateItemRequest() {
        UserDto userDto1 = UserDto.builder()
                .id(2L)
                .name("USER")
                .email("yampl@exmdt.com")
                .build();
        UserDto user = userService.createUser(userDto1);

        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Comp")
                .owner(UserMapper.toUser(user))
                .available(true)
                .description("Comp")
                .build();
        ItemDto itemDto1 = itemService.createItem(itemDto, user.getId());

        ItemDto itemDto3 = ItemDto.builder()
                .id(3L)
                .name("Comp")
                .owner(UserMapper.toUser(user))
                .available(true)
                .description("Comp")
                .build();
        ItemDto itemDto2 = itemService.createItem(itemDto, user.getId());

        List<ItemDto> itemDtos = new ArrayList<>();

        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(2L)
                .description("Thing")
                .requester(UserMapper.toUser(user))
                .created(LocalDateTime.now())
                .build();

        ItemRequestDto itemRequestDto1 = itemRequestService.createItemRequest(itemRequestDto, user.getId(),
                LocalDateTime.of(2022, 1, 2, 3, 4, 5));

        assertEquals(itemRequestDto.getId(), itemRequestDto1.getId());
        assertEquals(itemRequestDto.getRequester(), itemRequestDto1.getRequester());
        assertEquals(itemRequestDto.getDescription(), itemRequestDto1.getDescription());


    }

    @Test
    public void testGetItemRequest() {
        UserDto userDto1 = UserDto.builder()
                .id(2L)
                .name("USER")
                .email("yampl@exmdt.com")
                .build();
        UserDto user = userService.createUser(userDto1);

        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Comp")
                .owner(UserMapper.toUser(user))
                .available(true)
                .description("Comp")
                .build();
        ItemDto itemDto1 = itemService.createItem(itemDto, user.getId());

        ItemDto itemDto3 = ItemDto.builder()
                .id(3L)
                .name("Comp")
                .owner(UserMapper.toUser(user))
                .available(true)
                .description("Comp")
                .build();
        ItemDto itemDto2 = itemService.createItem(itemDto, user.getId());

        List<ItemDto> itemDtos = new ArrayList<>();


        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("Thing")
                .requester(UserMapper.toUser(user))
                .created(LocalDateTime.now())
                .items(itemDtos.stream().collect(Collectors.toList()))
                .build();

        itemRequestService.createItemRequest(itemRequestDto, user.getId(), LocalDateTime.of(2022, 1, 2, 3, 4, 5));


        ItemRequestDto itemRequestDto1 = itemRequestService.getItemRequestById(itemRequestDto.getId(), user.getId());

        assertEquals(itemRequestDto.getId(), itemRequestDto1.getId());
        assertEquals(itemRequestDto.getRequester(), itemRequestDto1.getRequester());
        assertEquals(itemRequestDto.getDescription(), itemRequestDto1.getDescription());
    }

    @Test
    public void testItemRequestWithWrongUserId() {
        UserDto userDto1 = UserDto.builder()
                .id(2L)
                .name("USER")
                .email("yampl@exmdt.com")
                .build();

        List<ItemDto> itemDtos = new ArrayList<>();

        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("Thing")
                .requester(UserMapper.toUser(userDto1))
                .created(LocalDateTime.now())
                .items(itemDtos.stream().collect(Collectors.toList()))
                .build();

        NotFoundException exp = assertThrows(NotFoundException.class,
                () -> itemRequestService.createItemRequest(itemRequestDto, -2L,
                        LocalDateTime.of(2022, 1, 2, 3, 4, 5)));
        assertEquals("User with ID= -2  doesn't exist", exp.getMessage());
    }

    @Test
    public void testCheckUser() {
        NotFoundException exp = assertThrows(NotFoundException.class,
                () -> userService.checkUser(-2L));
        assertEquals("User with ID= -2 doesn't exist", exp.getMessage());
    }

    @Test
    public void shouldExceptionWhenUpdateUserWithExistEmail() {
        UserDto userDto1 = UserDto.builder()
                .id(2L)
                .name("USER")
                .email("second@second.ru")
                .build();

        userService.createUser(userDto1);

        UserDto newUser = UserDto.builder()
                .id(3L)
                .name("User3")
                .email("third@third.ru")
                .build();

        UserDto returnUserDto = userService.createUser(newUser);
        Long id = returnUserDto.getId();

        returnUserDto.setId(null);
        returnUserDto.setEmail("second@second.ru");
        final RegisterException exception = Assertions.assertThrows(
                RegisterException.class,
                () -> userService.updateUser(returnUserDto, id));
        Assertions.assertEquals("User with E-mail=second@second.ru exists",
                exception.getMessage());
    }

    @Test
    public void testOfMappers() {
        UserDto userDto1 = UserDto.builder()
                .id(2L)
                .name("USER")
                .email("second@second.ru")
                .build();

        User user = UserMapper.toUser(userDto1);

        ItemDto itemDto3 = ItemDto.builder()
                .id(3L)
                .name("Comp")
                .owner(user)
                .available(true)
                .description("Comp")
                .build();

        ItemDtoFull itemDtoFull = ItemMapper.toItemDtoFull(ItemMapper.toItem(itemDto3), null, null,
                Collections.emptyList());

        assertEquals(3L, itemDtoFull.getId());
        assertEquals("Comp", itemDtoFull.getDescription());
        assertNotNull(itemDtoFull.getOwner());
        assertNull(itemDtoFull.getLastBooking());
        assertNull(itemDtoFull.getNextBooking());
        assertEquals(Collections.emptyList(), itemDtoFull.getComments());
    }

    @Test
    public void testGetAllItemRequest() {
        UserDto userDto1 = UserDto.builder()
                .id(1L)
                .name("USER")
                .email("100yampqwq21wl@exmdt.com")
                .build();

        UserDto user = userService.createUser(userDto1);

        UserDto userDto2 = UserDto.builder()
                .id(4L)
                .name("USER")
                .email("qwqw1212yampl@exmdt.com")
                .build();

        UserDto userDto = userService.createUser(userDto2);

        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Comp")
                .owner(UserMapper.toUser(user))
                .available(true)
                .description("Comp")
                .build();

        List<ItemDto> itemDtos = new ArrayList<>();

        itemDtos.add(itemService.createItem(itemDto, user.getId()));

        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(2L)
                .description("Interesting Thing")
                .requester(UserMapper.toUser(userDto))
                .created(LocalDateTime.now())
                .items(itemDtos)
                .build();

        assertEquals(itemRequestDto.getId(), ItemRequestMapper.toItemRequest(itemRequestDto).getId());
        assertEquals(itemRequestDto.getDescription(), ItemRequestMapper.toItemRequest(itemRequestDto).getDescription());
        assertEquals(itemRequestDto.getRequester(), ItemRequestMapper.toItemRequest(itemRequestDto).getRequester());
        assertEquals(itemRequestDto.getCreated(), ItemRequestMapper.toItemRequest(itemRequestDto).getCreated());

        itemRequestService.createItemRequest(itemRequestDto, user.getId(),
                LocalDateTime.of(2022, 1, 2, 3, 4, 5));

        List<ItemRequestDto> itemRequestDtos = new ArrayList<>(itemRequestService.getAllItemRequest(user.getId(),
                0, 10));


        assertTrue(itemRequestDtos.size() <= 10);
        assertEquals(3, itemRequestDtos.size());
    }

    @Test
    public void testGetAllBookings() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("USER")
                .email("yaml@exmdt.com")
                .build();
        userService.createUser(userDto);

        UserDto userDto1 = UserDto.builder()
                .id(2L)
                .name("USER")
                .email("yampl@exmdt.com")
                .build();
        userService.createUser(userDto1);

        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Comep")
                .owner(UserMapper.toUser(userDto))
                .available(true)
                .description("Comep")
                .build();

        itemService.createItem(itemDto, userDto.getId());

        BookingInputDTO bookingInputDTO = BookingInputDTO.builder()
                .itemId(itemDto.getId())
                .start(LocalDateTime.of(2023, 11, 10, 11, 11))
                .end(LocalDateTime.of(2024, 11, 11, 12, 51))
                .build();

        BookingDto bookingDto = bookingService.createBooking(bookingInputDTO, userDto1.getId());


        List<BookingDto> bookingDtos = new ArrayList<>();

        bookingDto.setId(1L);
        bookingDtos.add(bookingDto);

        assertEquals(bookingDtos.size(),
                bookingService.getAllBookingsByUserId("ALL", userDto1.getId(), 0, 1).size());
        assertEquals(0,
                bookingService.getAllBookingsByUserId("CURRENT", userDto1.getId(), 0, 1).size());
        assertEquals(bookingDtos.size(),
                bookingService.getAllBookingsByUserId("PAST", userDto1.getId(), 0, 1).size());
        assertEquals(bookingDtos.size(),
                bookingService.getAllBookingsByUserId("FUTURE", userDto1.getId(), 0, 1).size());
        assertEquals(bookingDtos.size(),
                bookingService.getAllBookingsByUserId("WAITING", userDto1.getId(), 0, 1).size());
        assertEquals(0,
                bookingService.getAllBookingsByUserId("REJECTED", userDto1.getId(), 0, 1).size());
        assertEquals(bookingDtos, bookingService.getAllBookingsByUserId("ALL", userDto1.getId(), 0, 1));
    }

    @Test
    public void testCommentCreate() {

        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("USER")
                .email("yaml@exmdt.com")
                .build();


        UserDto userDto1 = UserDto.builder()
                .id(2L)
                .name("USER")
                .email("yampl@exmdt.com")
                .build();


        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Comep")
                .owner(null)
                .available(true)
                .description("Comep")
                .build();

        UserDto ownerDto = userService.createUser(userDto);

        UserDto newUserDto = userService.createUser(userDto1);

        ItemDto newItemDto = itemService.createItem(itemDto, ownerDto.getId());

        BookingInputDTO bookingInputDto = BookingInputDTO.builder()
                .itemId(newItemDto.getId())
                .start(LocalDateTime.now().plusSeconds(1))
                .end(LocalDateTime.now().plusSeconds(3))
                .build();

        BookingDto bookingDto = bookingService.createBooking(bookingInputDto, 2L);
        bookingService.updateBooking(bookingDto.getId(), ownerDto.getId(), true);

        try {
            sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        InputCommentDto commentDto = InputCommentDto.builder()
                .id(1L)
                .text("Comment1")
                .itemId(1L)
                .created(LocalDateTime.now())
                .build();

        CommentDto commentDto1 = itemService.createNewComment(commentDto, 2L, 1L);
        List<CommentDto> commentDtos = new ArrayList<>();
        commentDtos.add(commentDto1);
        List<List<CommentDto>> comments = new ArrayList<>();
        assertNotNull(commentDto1);
        assertEquals(1L, commentDto1.getId());
        assertEquals(newUserDto.getName(), commentDto1.getAuthorName());
        assertEquals(itemService.getItemById(1L, 2L).getComments().size(), commentDtos.size());
        assertEquals(itemService.getAllItemsByUserId(2L, 0, 10).stream()
                .mapToInt(item -> item.getComments().size())
                .sum(), 0);
    }

    @Test
    public void testDeleteItemById() {
        UserDto userDto1 = UserDto.builder()
                .id(1L)
                .name("USER")
                .email("yampl@exmdt.com")
                .build();

        userService.createUser(userDto1);

        ItemDto itemDto1 = ItemDto.builder()
                .id(1L)
                .name("wqwq")
                .owner(UserMapper.toUser(userDto1))
                .available(true)
                .description("Coqwqwmep")
                .build();
        itemService.createItem(itemDto1, userDto1.getId());

        ItemDto itemDto = ItemDto.builder()
                .id(2L)
                .name("wqwq")
                .owner(UserMapper.toUser(userDto1))
                .available(true)
                .description("Coqwqwmep")
                .build();

        itemService.createItem(itemDto, userDto1.getId());

        itemService.deleteItem(itemDto.getId());

        assertEquals(1, itemService.getAllItemsByUserId(userDto1.getId(), 0, 10).size());
    }

    @Test
    public void testCreateCommentWithoutBooking() {

        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("USER")
                .email("yaml@exmdt.com")
                .build();


        UserDto userDto1 = UserDto.builder()
                .id(2L)
                .name("USER")
                .email("yampl@exmdt.com")
                .build();


        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Comep")
                .owner(null)
                .available(true)
                .description("Comep")
                .build();

        InputCommentDto commentDto = InputCommentDto.builder()
                .id(1L)
                .text("Comment1")
                .itemId(1L)
                .created(LocalDateTime.now())
                .build();

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> itemService.createNewComment(commentDto, 2L, 1L));

        assertEquals("You can't comment Item without booking", exception.getMessage());
    }


    @Test
    public void testGetBookingByStatus() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("USER")
                .email("yaml@exmdt.com")
                .build();
        userService.createUser(userDto);

        UserDto userDto1 = UserDto.builder()
                .id(2L)
                .name("USER")
                .email("yampl@exmdt.com")
                .build();
        userService.createUser(userDto1);

        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Comep")
                .owner(UserMapper.toUser(userDto))
                .available(true)
                .description("Comep")
                .build();

        itemService.createItem(itemDto, userDto.getId());

        BookingInputDTO bookingInputDTO21 = BookingInputDTO.builder()
                .itemId(itemDto.getId())
                .start(LocalDateTime.of(2023, 11, 10, 11, 11))
                .end(LocalDateTime.of(2024, 11, 11, 12, 51))
                .build();

        BookingDto bookingDto = bookingService.createBooking(bookingInputDTO21, userDto1.getId());


        assertEquals(0,
                bookingService.getBookingsByOwner("ALL", userDto1.getId(), 0, 1).size());
        assertEquals(0,
                bookingService.getBookingsByOwner("CURRENT", userDto1.getId(), 0, 1).size());
        assertEquals(0,
                bookingService.getBookingsByOwner("PAST", userDto1.getId(), 0, 1).size());
        assertEquals(0,
                bookingService.getBookingsByOwner("FUTURE", userDto1.getId(), 0, 1).size());
        assertEquals(0,
                bookingService.getBookingsByOwner("WAITING", userDto1.getId(), 0, 1).size());
        assertEquals(0,
                bookingService.getBookingsByOwner("REJECTED", userDto1.getId(), 0, 1).size());
    }

    @Test
    public void testGetOwnItemRequest() {
        UserDto userDto1 = UserDto.builder()
                .id(5L)
                .name("USER")
                .email("1000yampqwqwl@exmdt.com")
                .build();

        UserDto user = userService.createUser(userDto1);

        UserDto userDto2 = UserDto.builder()
                .id(4L)
                .name("USER")
                .email("qwqw1212yampl@exmdt.com")
                .build();

        UserDto userDto = userService.createUser(userDto2);

        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Comp")
                .owner(UserMapper.toUser(user))
                .available(true)
                .description("Comp")
                .build();

        List<ItemDto> itemDtos = new ArrayList<>();

        itemDtos.add(itemService.createItem(itemDto, user.getId()));

        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("Interesting Thing")
                .requester(UserMapper.toUser(userDto))
                .created(LocalDateTime.now())
                .items(itemDtos)
                .build();

        itemRequestService.createItemRequest(itemRequestDto, user.getId(),
                LocalDateTime.of(2022, 1, 2, 3, 4, 5));

        List<ItemRequestDto> itemRequestDtos = new ArrayList<>(itemRequestService.getOwnItemRequests(user.getId(),
                0, 10));

        assertEquals(1, itemRequestDtos.size());
    }

    @Test
    public void testGetAllUser() {
        List<UserDto> userDtos = new ArrayList<>(userService.getAll());
        assertEquals(2, userService.getAll().size());
        assertEquals(userDtos, userService.getAll());
    }

}