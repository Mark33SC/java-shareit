package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemClient itemClient;
    private static final String header = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestHeader(header) long userId,
            @RequestBody @NonNull @Valid Item item) {
        return itemClient.create(item, userId);
    }

    @PatchMapping("{itemId}")
    public ResponseEntity<Object> update(
            @RequestHeader(header) long userId,
            @RequestBody @NonNull ItemDto itemDto,
            @PathVariable long itemId) {
        return itemClient.update(itemDto, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(
            @PathVariable("itemId") long itemId,
            @RequestHeader(header) long userId) {
        return itemClient.getItem(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUserItem(
            @RequestHeader(header) long userId,
            @RequestParam(required = false) @PositiveOrZero Integer from,
            @RequestParam(required = false) @Positive Integer size) {
        return itemClient.getAllUserItems(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam String text) {
        return itemClient.searchAvailableItems(text);
    }

    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> addComment(
            @RequestHeader(header) long userId,
            @RequestBody @NonNull @Valid CommentCreateDto comment,
            @PathVariable long itemId) {
        return itemClient.addComment(userId, comment, itemId);
    }


}
