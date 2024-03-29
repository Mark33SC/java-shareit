package ru.practicum.item;

import ru.practicum.item.comment.Comment;
import ru.practicum.item.comment.dto.CommentCreateDto;
import ru.practicum.item.dto.ItemWithBookingDatesDto;
import ru.practicum.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    List<ItemWithBookingDatesDto> getAllItemsOfOwner(long ownerId, int from, int size);

    Item getItemById(long itemId);

    ItemWithBookingDatesDto getItemById(long itemId, long userId);

    List<ItemDto> searchItems(String text, Integer from, Integer size);

    Item addItem(long ownerId, Item item);

    Item updateItem(long ownerId, ItemDto itemDto);

    void deleteById(long ownerId, long id);

    Comment addComment(CommentCreateDto commentCreateDto, long itemId, long userId);

    List<Item> findAllByRequestId(long requestId);
}
