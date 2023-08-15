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
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureCache
class ShareItTests {

    UserService userService;

    ItemService itemService;

	ItemMapper itemMapper;

	BookingService bookingService;

	BookingMapper bookingMapper;

    @Autowired
    public ShareItTests(UserService userService, ItemService itemService, ItemMapper itemMapper,
						BookingService bookingService, BookingMapper bookingMapper) {
        this.userService = userService;
        this.itemService = itemService;
		this.bookingService = bookingService;
		this.bookingMapper = bookingMapper;
		this.itemMapper = itemMapper;

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

        Assertions.assertEquals(userDto, userService.createUser(userDto));
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
		Assertions.assertEquals(userDto, userService.updateUser(userDto, userDto.getId()));
	}

    @Test
    public void testUserGetId() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("USER")
                .email("user16@example.com")
                .build();

        userService.createUser(userDto);
        Assertions.assertEquals(userDto, userService.getUserById(userDto.getId()));
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

		Assertions.assertEquals(useDto, userService.getUserById(useDto.getId()));

		Assertions.assertEquals(itemDto.getId(), itemService.getItemById(itemDto.getId(), useDto.getId()).getId());

		Assertions.assertEquals(itemDto.getDescription(),
				itemService.getItemById(itemDto.getId(), useDto.getId()).getDescription());

		Assertions.assertEquals(itemDto.getAvailable(),
				itemService.getItemById(itemDto.getId(), useDto.getId()).getAvailable());

		Assertions.assertEquals(itemDto.getOwner(),
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

		Assertions.assertEquals(itemDto, itemService.updateItem(itemDto, itemDto.getId(), userDto.getId()));
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

		Assertions.assertEquals(userDto, userService.getUserById(userDto.getId()));
		Assertions.assertEquals(itemDto.getId(), itemService.getItemById(itemDto.getId(), userDto.getId()).getId());
		Assertions.assertEquals(itemDto.getDescription(), itemService.getItemById(itemDto.getId(), userDto.getId()).getDescription());
		Assertions.assertEquals(itemDto.getAvailable(), itemService.getItemById(itemDto.getId(), userDto.getId()).getAvailable());
		Assertions.assertEquals(itemDto.getOwner(), itemService.getItemById(itemDto.getId(), userDto.getId()).getOwner());
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

		Assertions.assertEquals(userDto, userService.getUserById(userDto.getId()));
		Assertions.assertEquals(itemDtos, itemService.getItemByDescription(itemDto.getName(), 0, 10));
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

		Assertions.assertEquals(UserMapper.toUser(userDto), user);
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

		Assertions.assertEquals(bookingDto1, bookingDto);

		BookingDto bookingDto2 = bookingService.updateBooking(bookingDto.getId(), userDto.getId(), true);

		Assertions.assertEquals(Status.APPROVED, bookingDto2.getStatus());
	}
}
