package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public Item addItem(long ownerId, Item item) throws UserNotFoundException {
        return itemRepository.addItem(ownerId, item);
    }

    @Override
    public Collection<Item> getAllItemsOfOwner(long ownerId) throws UserNotFoundException {
        return itemRepository.getAllItemsOfOwner(ownerId);
    }

    @Override
    public Item getById(long id) throws ItemNotFoundException {
        return itemRepository.getById(id);
    }

    @Override
    public Item updateItem(ItemDto itemDto, long ownerId, long itemId) throws UserNotFoundException, ItemNotFoundException {
        Item item = itemRepository.getById(itemId);

        Item itemCopy = new Item(item.getId(), item.getName(),
                item.getDescription(), item.getOwnerId(),
                item.getAvailable(), item.getRequest(), item.getBookingsId());
        String newName = itemDto.getName();
        String newDescription = itemDto.getDescription();
        Boolean newAvailability = itemDto.getAvailable();
        if (newName != null) {
            itemCopy.setName(newName);
        }
        if (newDescription != null) {
            itemCopy.setDescription(newDescription);
        }
        if (newAvailability != null) {
            itemCopy.setAvailable(newAvailability);
        }
        return itemRepository.updateItem(itemCopy, ownerId, itemId);
    }

    @Override
    public void deleteById(long ownerId, long id) throws UserNotFoundException {
        itemRepository.deleteById(ownerId, id);
    }

    @Override
    public Collection<Item> searchItems(String text) {
        return itemRepository.searchItems(text);
    }
}