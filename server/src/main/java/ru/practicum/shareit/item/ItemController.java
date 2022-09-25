package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.comment.dto.CommentCreateDto;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentDtoMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemWithBookingDatesDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
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
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "100") @Min(1) Integer size
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
            @Valid @Min(0) @RequestParam(required = false) Integer from,
            @Valid @Min(1) @RequestParam(required = false) Integer size
    ) {
        return itemService.searchItems(text, from, size);
    }

    @PostMapping
    public ItemDto addItem(
            @RequestHeader(header) long userId,
            @Valid @RequestBody Item item
    ) {
        return ItemMapper.mapToDto(itemService.addItem(userId, item));
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(
            @Valid @RequestBody CommentCreateDto commentCreateDto,
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