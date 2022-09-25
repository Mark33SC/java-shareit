package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final ItemDtoMapper itemDtoMapper;

    @GetMapping
    public Collection<ItemDto> getAllItemsOfOwner(@RequestHeader("X-Sharer-User-Id") long ownerId) throws UserNotFoundException {
        return itemService.getAllItemsOfOwner(ownerId).stream()
                .map(itemDtoMapper::mapToDto).collect(Collectors.toList());
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@PathVariable long itemId) throws UserNotFoundException, ItemNotFoundException {
        return itemDtoMapper.mapToDto(itemService.getById(itemId));
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItems(@RequestParam String text) {
        return itemService.searchItems(text).stream()
                .map(itemDtoMapper::mapToDto).collect(Collectors.toList());
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") long ownerId,
                           @RequestBody @Valid Item item) throws UserNotFoundException {
        return itemDtoMapper.mapToDto(itemService.addItem(ownerId, item));
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateById(@RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") long ownerId,
                              @PathVariable long itemId) throws UserNotFoundException, ItemNotFoundException {
        return itemDtoMapper.mapToDto(itemService.updateItem(itemDto, ownerId, itemId));
    }

    @DeleteMapping("/{itemId}")
    public void deleteById(@RequestHeader("X-Sharer-User-Id") long ownerId,
                           @PathVariable long itemId) throws UserNotFoundException {
        itemService.deleteById(ownerId, itemId);
    }
}