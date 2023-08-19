package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;

import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDTO;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.RegisterException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureCache
class ShareItTests {

    UserService userService;

    ItemService itemService;

    ItemMapper itemMapper;

    BookingService bookingService;

    ItemRequestService itemRequestService;

    BookingMapper bookingMapper;


    @Autowired
    public ShareItTests(UserService userService, ItemService itemService, ItemMapper itemMapper,
                        BookingService bookingService, ItemRequestService itemRequestService,
                        BookingMapper bookingMapper) {
        this.userService = userService;
        this.itemService = itemService;
        this.itemMapper = itemMapper;
        this.bookingService = bookingService;
        this.itemRequestService = itemRequestService;
        this.bookingMapper = bookingMapper;
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

        ItemRequestDto itemRequestDto1 = itemRequestService.createItemRequest(itemRequestDto, user.getId(), LocalDateTime.of(2022, 1, 2, 3, 4, 5));

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
    void testItemRequestWithWrongUserId() {
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
    void shouldExceptionWhenUpdateUserWithExistEmail() {
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
}
