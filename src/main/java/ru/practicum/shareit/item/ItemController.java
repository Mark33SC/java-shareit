package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.comment.dto.CommentCreateDto;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentDtoMapper;
import ru.practicum.shareit.item.dto.ItemCreateDto;
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

    @GetMapping
    public List<ItemWithBookingDatesDto> getAll(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "100") @Min(1) Integer size
    ) {
        return itemService.getAllItemsOfOwner(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ItemWithBookingDatesDto getById(
            @PathVariable long itemId,
            @RequestHeader("X-Sharer-User-Id") long userId
    ) {

        return itemService.getItemById(itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(
            @RequestParam() String text
    ) {
        return itemService.searchItems(text);
    }

    @PostMapping
    public ItemDto addItem(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @Valid @RequestBody ItemCreateDto itemCreateDto
    ) {
        return ItemMapper.mapToDto(itemService.addItem(userId, itemCreateDto));
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(
            @Valid @RequestBody CommentCreateDto commentCreateDto,
            @PathVariable long itemId,
            @RequestHeader("X-Sharer-User-Id") long userId
    ) {
        return CommentDtoMapper.mapToDto(itemService.addComment(commentCreateDto, itemId, userId));
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateById(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long itemId,
            @RequestBody ItemDto itemDto
    ) {
        itemDto.setId(itemId);

        return ItemMapper.mapToDto(itemService.updateItem(userId, itemDto));
    }

    @DeleteMapping("/{itemId}")
    public void deleteById(@RequestHeader("X-Sharer-User-Id") long ownerId,
                           @PathVariable long itemId) throws UserNotFoundException {
        itemService.deleteById(ownerId, itemId);
    }
}