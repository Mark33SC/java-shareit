package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Generators;
import ru.practicum.shareit.exceptions.CommentValidationException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.exceptions.UserNotOwnerItemException;
import ru.practicum.shareit.item.comment.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceTest {
    @Autowired
    private final ItemService itemService;

    @Autowired
    private final TestEntityManager testEntityManager;

    @BeforeEach
    public void beforeEachItemServiceTest() {
        testEntityManager.clear();
    }

    @Test
    public void createItem() {
        Item item = Generators.ITEM_SUPPLIER.get();
        Long ownerId = testEntityManager.persistAndGetId(item.getOwner(), Long.class);
        testEntityManager.flush();

        Item createdItem = itemService.addItem(ownerId, item);
        Item foundItem = testEntityManager.find(Item.class, createdItem.getId());

        assertEquals(createdItem, foundItem);
    }

    @Test
    public void createFailedItemByWrongUserId() {
        Item item = Generators.ITEM_SUPPLIER.get();

        assertThrows(UserNotFoundException.class, () -> itemService.addItem(10, item));
    }

    @Test
    public void createItemWithEmptyName() {
        Item item = Generators.ITEM_SUPPLIER.get();
        item.setName("");
        Long ownerId = testEntityManager.persistAndGetId(item.getOwner(), Long.class);
        testEntityManager.flush();

        assertThrows(ConstraintViolationException.class, () -> itemService.addItem(ownerId, item));
    }

    @Test
    public void createItemWithEmptyDescription() {
        Item item = Generators.ITEM_SUPPLIER.get();
        item.setDescription("");
        Long ownerId = testEntityManager.persistAndGetId(item.getOwner(), Long.class);
        testEntityManager.flush();

        assertThrows(ConstraintViolationException.class, () -> itemService.addItem(ownerId, item));
    }

    @Test
    public void itemUpdate() {
        Item item = Generators.ITEM_SUPPLIER.get();
        Long ownerId = testEntityManager.persistAndGetId(item.getOwner(), Long.class);
        Long itemId = testEntityManager.persistAndGetId(item, Long.class);
        testEntityManager.flush();

        ItemDto updatedItemDto = ItemDto.builder()
                .id(itemId)
                .name("Дрель+")
                .description("Аккумуляторная дрель")
                .available(false)
                .build();

        assertAll(
                () -> assertDoesNotThrow(() -> itemService.updateItem(ownerId, updatedItemDto)),
                () -> assertEquals(testEntityManager.find(Item.class, itemId).getName(), updatedItemDto.getName())
        );
    }

    @Test
    public void updateItemWithOtherUser() {
        Item item = Generators.ITEM_SUPPLIER.get();
        testEntityManager.persistAndGetId(item.getOwner(), Long.class);
        long otherUserId = testEntityManager.persistAndGetId(Generators.USER_SUPPLIER.get(), Long.class);
        Long itemId = testEntityManager.persistAndGetId(item, Long.class);
        testEntityManager.flush();

        ItemDto updatedItemDto = ItemDto.builder()
                .id(itemId)
                .name("Дрель")
                .description("Простая дрель")
                .available(false)
                .build();

        assertThrows(UserNotOwnerItemException.class, () -> itemService.updateItem(otherUserId, updatedItemDto));
    }

    @Test
    public void updateItemAvailable() {
        Item item = Generators.ITEM_SUPPLIER.get();
        item.setAvailable(false);
        Long ownerId = testEntityManager.persistAndGetId(item.getOwner(), Long.class);
        Long itemId = testEntityManager.persistAndGetId(item, Long.class);
        testEntityManager.flush();

        ItemDto updatedItemDto = ItemDto.builder()
                .id(itemId)
                .available(true)
                .build();

        assertAll(
                () -> assertFalse(testEntityManager.find(Item.class, itemId).getAvailable()),
                () -> assertDoesNotThrow(() -> itemService.updateItem(ownerId, updatedItemDto)),
                () -> assertTrue(testEntityManager.find(Item.class, itemId).getAvailable())
        );
    }

    @Test
    public void updateItemDescription() {
        Item item = Generators.ITEM_SUPPLIER.get();
        Long ownerId = testEntityManager.persistAndGetId(item.getOwner(), Long.class);
        Long itemId = testEntityManager.persistAndGetId(item, Long.class);
        testEntityManager.flush();
        String updatedDescription = "обновление";


        ItemDto updatedItemDto = ItemDto.builder()
                .id(itemId)
                .description(updatedDescription)
                .build();

        assertAll(
                () -> assertNotEquals(testEntityManager.find(Item.class, itemId).getDescription(), updatedDescription),
                () -> assertDoesNotThrow(() -> itemService.updateItem(ownerId, updatedItemDto)),
                () -> assertEquals(testEntityManager.find(Item.class, itemId).getDescription(), updatedDescription)
        );
    }

    @Test
    public void updateItemName() {
        Item item = Generators.ITEM_SUPPLIER.get();
        Long ownerId = testEntityManager.persistAndGetId(item.getOwner(), Long.class);
        Long itemId = testEntityManager.persistAndGetId(item, Long.class);
        testEntityManager.flush();
        String updatedName = "новое имя";


        ItemDto updatedItemDto = ItemDto.builder()
                .id(itemId)
                .name(updatedName)
                .build();

        assertAll(
                () -> assertNotEquals(testEntityManager.find(Item.class, itemId).getName(), updatedName),
                () -> assertDoesNotThrow(() -> itemService.updateItem(ownerId, updatedItemDto)),
                () -> assertEquals(testEntityManager.find(Item.class, itemId).getName(), updatedName)
        );
    }

    @Test
    public void getItem() {
        Item item = Generators.ITEM_SUPPLIER.get();
        Long ownerId = testEntityManager.persistAndGetId(item.getOwner(), Long.class);
        Long itemId = testEntityManager.persistAndGetId(item, Long.class);
        testEntityManager.flush();

        assertEquals(itemService.getItemById(itemId, ownerId).getName(), item.getName());
    }

    @Test
    public void getUnknownItem() {
        Item item = Generators.ITEM_SUPPLIER.get();
        Long ownerId = testEntityManager.persistAndGetId(item.getOwner(), Long.class);
        testEntityManager.persistAndGetId(item, Long.class);
        testEntityManager.flush();

        assertThrows(ItemNotFoundException.class, () -> itemService.getItemById(100, ownerId));
    }

    @Test
    public void getAllItem() {
        Item item1 = Generators.ITEM_SUPPLIER.get();
        Item item2 = Generators.ITEM_SUPPLIER.get();
        item2.setOwner(item1.getOwner());

        Long ownerId = testEntityManager.persistAndGetId(item1.getOwner(), Long.class);
        testEntityManager.persist(item1);
        testEntityManager.persist(item2);
        testEntityManager.flush();

        assertEquals(itemService.getAllItemsOfOwner(ownerId, 0, Integer.MAX_VALUE).size(), 2);
    }

    @Test
    public void searchItemByDescription() {
        String searchTxt = "батАРе";
        Item item1 = Generators.ITEM_SUPPLIER.get();
        item1.setDescription("Зарядная батарея");
        Item item2 = Generators.ITEM_SUPPLIER.get();
        item2.setName("Батарейка");
        Item item3 = Generators.ITEM_SUPPLIER.get();

        testEntityManager.persist(item1.getOwner());
        testEntityManager.persist(item1);
        testEntityManager.persist(item2.getOwner());
        testEntityManager.persist(item2);
        testEntityManager.persist(item3.getOwner());
        testEntityManager.persist(item3);
        testEntityManager.flush();

        assertEquals(itemService.searchItems(searchTxt, 0, Integer.MAX_VALUE).size(), 2);
    }

    @Test
    public void searchEmptyItem() {
        String searchTxt = "";
        Item item1 = Generators.ITEM_SUPPLIER.get();
        item1.setDescription("Зарядная батарея");
        Item item2 = Generators.ITEM_SUPPLIER.get();
        item2.setName("Батарейка");
        Item item3 = Generators.ITEM_SUPPLIER.get();

        testEntityManager.persist(item1.getOwner());
        testEntityManager.persist(item1);
        testEntityManager.persist(item2.getOwner());
        testEntityManager.persist(item2);
        testEntityManager.persist(item3.getOwner());
        testEntityManager.persist(item3);
        testEntityManager.flush();

        assertEquals(itemService.searchItems(searchTxt, 0, Integer.MAX_VALUE).size(), 0);
    }

    @Test
    public void addCommentToItemWithoutBookingFailed() {
        Item item = Generators.ITEM_SUPPLIER.get();
        User user = Generators.USER_SUPPLIER.get();
        testEntityManager.persist(item.getOwner());
        Long itemId = testEntityManager.persistAndGetId(item, Long.class);
        Long userId = testEntityManager.persistAndGetId(user, Long.class);
        testEntityManager.flush();

        CommentCreateDto comment = new CommentCreateDto("комментарий");

        assertThrows(CommentValidationException.class, () -> itemService.addComment(comment, itemId, userId));
    }

}
