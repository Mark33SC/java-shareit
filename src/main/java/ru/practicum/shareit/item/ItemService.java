package ru.practicum.shareit.item;

import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDatesDto;

import java.util.List;

public interface ItemService {
    List<ItemWithBookingDatesDto> getAllItemsOfOwner(long ownerId, int from, int size);

    Item getItemById(long itemId);

    ItemWithBookingDatesDto getItemById(long itemId, long userId);

    List<ItemDto> searchItems(String txt);

    Item addItem(long ownerId, ItemCreateDto itemCreateDto);

    Item updateItem(long ownerId, ItemDto itemDto);

    void deleteById(long ownerId, long id);

    Comment addComment(CommentCreateDto commentCreateDto, long itemId, long userId);
}
