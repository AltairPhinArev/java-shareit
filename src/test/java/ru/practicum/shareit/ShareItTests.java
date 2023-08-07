package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;

import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;

import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureCache
class ShareItTests {

    UserService userService;

    UserMapper userMapper;

    ItemService itemService;

	ItemMapper itemMapper;

	BookingService bookingService;

	BookingMapper bookingMapper;


    @Autowired
    public ShareItTests(UserService userService, UserMapper userMapper, ItemService itemService, ItemMapper itemMapper,
						BookingService bookingService, BookingMapper bookingMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
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
                .email("user16example.com")
                .build();

        userService.createUser(userDto);
        Assertions.assertEquals(userDto, userService.getUserById(userDto.getId()));
    }

	@Test
	public void testUserDelete() {
		UserDto userDto = UserDto.builder()
				.id(1L)
				.name("USER")
				.email("user17@example.com")
				.build();

		userService.createUser(userDto);
		Assertions.assertEquals(userDto, userService.getUserById(userDto.getId()));
		int size = userService.getAll().size();

		userService.deleteUserById(userDto.getId());
		Assertions.assertEquals(size - 1, userService.getAll().size());
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
                .owner(userMapper.toUser(useDto))
                .available(true)
                .description("Disc")
                .build();
        userService.createUser(useDto);

		Assertions.assertEquals(useDto, userService.getUserById(useDto.getId()));
		Assertions.assertEquals(itemDto.getId(), itemService.getItemById(itemDto.getId(), useDto.getId()).getId());
		Assertions.assertEquals(itemDto.getDescription(), itemService.getItemById(itemDto.getId(), useDto.getId()).getDescription());
		Assertions.assertEquals(itemDto.getAvailable(), itemService.getItemById(itemDto.getId(), useDto.getId()).getAvailable());
		Assertions.assertEquals(itemDto.getOwner(), itemService.getItemById(itemDto.getId(), useDto.getId()).getOwner());
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
				.owner(userMapper.toUser(userDto))
				.available(true)
				.description("Disc")
				.build();
		userService.createUser(userDto);
		itemService.createItem(itemDto, userDto.getId());

		itemDto.setDescription("Descriptor");

		Assertions.assertEquals(itemDto, itemService.updateItem(itemDto, itemDto.getId(), userDto.getId()));
	}

    @Test
    public void testItemDelete() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("USER")
                .email("user20@example.com")
                .build();

        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Disc")
                .owner(userMapper.toUser(userDto))
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


        int size = itemService.getAllItems(userDto.getId()).size();

		itemService.deleteItem(itemDto.getId());

		Assertions.assertEquals(size - 1, itemService.getAllItems(userDto.getId()).size());
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
				.owner(userMapper.toUser(userDto))
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
				.owner(userMapper.toUser(userDto))
				.available(true)
				.description("Comp")
				.build();

		userService.createUser(userDto);
		itemService.createItem(itemDto, userDto.getId());
		List<ItemDto> itemDtos = new ArrayList<>();

		itemDtos.add(ItemMapper.toItemDtoFromFullItemDto(itemService.getItemById(itemDto.getId(), userDto.getId())));

		Assertions.assertEquals(userDto, userService.getUserById(userDto.getId()));
		Assertions.assertEquals(itemDtos, itemService.getItemByDescription(itemDto.getName()));
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

		Assertions.assertEquals(userMapper.toUser(userDto), user);
	}
}