package ru.practicum.shareit.negativeTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
public class NegativeItemTest {

    @Mock
    ItemRepository itemRepository;

    @Autowired
    ItemService itemService;

    @Mock
    UserService userService;

    @Test
    public void shouldExceptionWhenGetItemWithWrongId() {
        itemService = new ItemServiceImpl(itemRepository, null,
                null, null, null, null);

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.getItemById(-10L, 1L));
        Assertions.assertEquals("Item with ID=-10 doesn't exist", exception.getMessage());
    }

    @Test
    public void shouldExceptionWhenGetItemWithDescription() {
        ItemService itemService1 = new ItemServiceImpl(itemRepository, null,  null,
                null, null, null);
        when(userService.checkUser(any(Long.class)))
                .thenReturn(true);
        when(itemRepository.search(any(String.class), eq(PageRequest.of(0, 10))))
                .thenReturn(Collections.emptyList());

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> {
                    boolean userCheckResult = userService.checkUser(123L);
                    List<Item> items = itemRepository.search("description", PageRequest.of(0, 10));
                    itemService1.getItemByDescription(null, 0, -1 );
                });
        Assertions.assertEquals("Illegal params", exception.getMessage());
    }
}
