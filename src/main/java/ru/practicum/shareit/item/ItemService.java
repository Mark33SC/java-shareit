package ru.practicum.shareit.item;

import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    Collection<Item> getAllItemsOfOwner(long ownerId) throws UserNotFoundException;

    Item getById(long id) throws ItemNotFoundException;

    Collection<Item> searchItems(String text);

    Item addItem(long ownerId, Item item) throws UserNotFoundException;

    Item updateItem(ItemDto itemDto, long ownerId, long itemId) throws UserNotFoundException, ItemNotFoundException;

    void deleteById(long ownerId, long id) throws UserNotFoundException;
}
