package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exceptions.UserNotFoundException;
import ru.practicum.item.comment.dto.CommentCreateDto;
import ru.practicum.item.comment.dto.CommentDto;
import ru.practicum.item.comment.dto.CommentDtoMapper;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemMapper;
import ru.practicum.item.dto.ItemWithBookingDatesDto;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private static final String header = "X-Sharer-User-Id";

    @GetMapping
    public List<ItemWithBookingDatesDto> getAll(
            @RequestHeader(header) long userId,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return itemService.getAllItemsOfOwner(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ItemWithBookingDatesDto getById(
            @PathVariable("itemId") long itemId,
            @RequestHeader(header) long userId
    ) {

        return itemService.getItemById(itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(
            @RequestParam() String text,
            @RequestParam(required = false) Integer from,
            @RequestParam(required = false) Integer size
    ) {
        return itemService.searchItems(text, from, size);
    }

    @PostMapping
    public ItemDto addItem(
            @RequestHeader(header) long userId,
            @RequestBody Item item
    ) {
        return ItemMapper.mapToDto(itemService.addItem(userId, item));
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(
            @RequestBody CommentCreateDto commentCreateDto,
            @PathVariable long itemId,
            @RequestHeader(header) long userId
    ) {
        return CommentDtoMapper.mapToDto(itemService.addComment(commentCreateDto, itemId, userId));
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateById(
            @RequestHeader(header) long userId,
            @PathVariable long itemId,
            @RequestBody ItemDto itemDto
    ) {
        itemDto.setId(itemId);

        return ItemMapper.mapToDto(itemService.updateItem(userId, itemDto));
    }

    @DeleteMapping("/{itemId}")
    public void deleteById(@RequestHeader(header) long ownerId,
                           @PathVariable long itemId) throws UserNotFoundException {
        itemService.deleteById(ownerId, itemId);
    }
}