package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    private long itemId = 0;
    private final Map<Long, Set<Item>> items = new HashMap<>();
    private final UserRepository userRepository;

    @Override
    public Collection<Item> getAllItemsOfOwner(long ownerId) throws UserNotFoundException {
        validateUser(ownerId);
        return items.get(ownerId);
    }

    @Override
    public Item getById(long id) throws ItemNotFoundException {
        return items.values().stream()
                .flatMap(Collection::stream)
                .filter(item -> item.getId() == id)
                .findFirst()
                .orElseThrow(ItemNotFoundException::new);
    }

    @Override
    public Collection<Item> searchItems(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        return items.values().stream()
                .flatMap(Collection::stream)
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public Item addItem(long ownerId, Item item) throws UserNotFoundException {
        validateUser(ownerId);
        item.setId(++itemId);
        items.compute(ownerId, (userId, itemsList) -> {
            if (itemsList == null) {
                itemsList = new HashSet<>();
            }
            itemsList.add(item);
            return itemsList;
        });
        return item;
    }

    @Override
    public Item updateItem(Item item, long ownerId, long itemId) throws UserNotFoundException, ItemNotFoundException {
        validateUser(ownerId);
        if (items.get(ownerId) == null) {
            throw new ItemNotFoundException("У этого пользователя нет вещей");
        }
        if (!items.get(ownerId).contains(item)) {
            throw new ItemNotFoundException("Такой вещи нет в памяти ИЛИ эта вещь не принадлежит указанному владельцу");
        }
        items.computeIfPresent(ownerId, (userId, items) -> {
            if (items.remove(item)) {
                items.add(item);
            }
            return items;
        });
        return getById(itemId);
    }

    @Override
    public void deleteById(long ownerId, long id) throws UserNotFoundException {
        validateUser(ownerId);
        items.get(ownerId).remove(Item.builder()
                .id(id)
                .build());
    }

    private void validateUser(long userId) throws UserNotFoundException {
        userRepository.getById(userId);
    }
}