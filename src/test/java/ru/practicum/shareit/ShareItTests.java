package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;

import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureCache
class ShareItTests {

    UserService userService;

    UserMapper userMapper;

    ItemService itemService;

    UserStorage userStorage;

    ItemStorage itemStorage;

    ItemRequestService itemRequestService;

    ItemRequestMapper itemRequestMapper;

    @Autowired
    public ShareItTests(UserService userService, UserMapper userMapper, ItemService itemService,
                        UserStorage userStorage, ItemStorage itemStorage, ItemRequestService itemRequestService,
                        ItemRequestMapper itemRequestMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.itemService = itemService;
        this.userStorage = userStorage;
        this.itemStorage = itemStorage;
        this.itemRequestService = itemRequestService;
        this.itemRequestMapper = itemRequestMapper;
    }

    @Test
    void contextLoads() {
    }

    @Test
    public void testUserCreate() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("USER")
                .email("email@.com")
                .build();

        Assertions.assertEquals(userDto, userService.createUser(userDto));
    }

	@Test
	public void testUserUpdate() {
		UserDto userDto = UserDto.builder()
				.id(1L)
				.name("USER")
				.email("emaily@.con")
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
                .email("emaqil@.com")
                .build();

        userService.createUser(userDto);
        Assertions.assertEquals(userDto, userService.getUserById(userDto.getId()));
    }

	@Test
	public void testUserDelete() {
		UserDto userDto = UserDto.builder()
				.id(1L)
				.name("USER")
				.email("emaiil@.com")
				.build();

		userService.createUser(userDto);
		Assertions.assertEquals(userDto, userService.getUserById(userDto.getId()));
		int size = userService.getAllUsers().size();

		userService.deleteUser(userDto.getId());
		Assertions.assertEquals(size - 1, userService.getAllUsers().size());
	}

	@Test
    public void testItemCreate() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("USER")
                .email("emaiil@.com")
                .build();

        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Disc")
                .owner(userMapper.toUser(userDto))
                .available(true)
                .description("Disc")
                .request(null)
                .build();
        userService.createUser(userDto);
        Assertions.assertEquals(itemDto, itemService.createItem(itemDto, userDto.getId()));
    }

	@Test
	public void testItemUpdate() {
		UserDto userDto = UserDto.builder()
				.id(1L)
				.name("USER")
				.email("emaiil@.con")
				.build();

		ItemDto itemDto = ItemDto.builder()
				.id(1L)
				.name("Disc")
				.owner(userMapper.toUser(userDto))
				.available(true)
				.description("Disc")
				.request(null)
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
                .email("yamlr@.com")
                .build();

        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Disc")
                .owner(userMapper.toUser(userDto))
                .available(true)
                .description("Disc")
                .request(null)
                .build();

        userService.createUser(userDto);
        itemService.createItem(itemDto, userDto.getId());

        Assertions.assertEquals(userDto, userService.getUserById(userDto.getId()));
        Assertions.assertEquals(itemDto, itemService.getItemById(itemDto.getId()));

        int size = itemService.getAllItems(userDto.getId()).size();

		itemService.deleteItem(itemDto.getId());

		Assertions.assertEquals(size - 1, itemService.getAllItems(userDto.getId()).size());
    }

	@Test
	public void testItemGetById() {
		UserDto userDto = UserDto.builder()
				.id(1L)
				.name("USER")
				.email("yaml@.com")
				.build();

		ItemDto itemDto = ItemDto.builder()
				.id(1L)
				.name("Disc")
				.owner(userMapper.toUser(userDto))
				.available(true)
				.description("Disc")
				.request(null)
				.build();

		userService.createUser(userDto);
		itemService.createItem(itemDto, userDto.getId());

		Assertions.assertEquals(userDto, userService.getUserById(userDto.getId()));
		Assertions.assertEquals(itemDto, itemService.getItemById(itemDto.getId()));
	}

	@Test
	public void testItemGetByText() {
		UserDto userDto = UserDto.builder()
				.id(1L)
				.name("USER")
				.email("yaml@.com")
				.build();

		ItemDto itemDto = ItemDto.builder()
				.id(1L)
				.name("Comp")
				.owner(userMapper.toUser(userDto))
				.available(true)
				.description("Comp")
				.request(null)
				.build();

		userService.createUser(userDto);
		itemService.createItem(itemDto, userDto.getId());
		List<ItemDto> itemDtos = new ArrayList<>();

		itemDtos.add(itemService.getItemById(itemDto.getId()));

		Assertions.assertEquals(userDto, userService.getUserById(userDto.getId()));
		Assertions.assertEquals(itemDtos, itemService.getItemByDescription(itemDto.getName()));
	}
}