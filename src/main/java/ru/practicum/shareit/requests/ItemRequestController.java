package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.ItemRequestNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.requests.dto.ItemRequestCreateDto;
import ru.practicum.shareit.requests.dto.ItemRequestMapper;
import ru.practicum.shareit.requests.dto.ItemRequestWithResponsesDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private final ItemRequestMapper itemRequestMapper;

    @PostMapping
    public ItemRequest add(@RequestHeader(value = "X-Sharer-User-Id") long requesterId,
                           @Valid @RequestBody ItemRequestCreateDto itemRequestInputDto) throws UserNotFoundException {
        return itemRequestService.add(requesterId, itemRequestInputDto);
    }

    @GetMapping("/{requestId}")
    public ItemRequestWithResponsesDto getById(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                               @PathVariable long requestId)
            throws UserNotFoundException, ItemRequestNotFoundException {
        return itemRequestMapper.toDtoWithResponses(itemRequestService.getById(userId, requestId));
    }

    @GetMapping
    public Collection<ItemRequestWithResponsesDto> getAllByUserId(@RequestHeader(value = "X-Sharer-User-Id") long requesterId)
            throws UserNotFoundException {
        return itemRequestService.getAllByRequesterId(requesterId)
                .stream().map(itemRequestMapper::toDtoWithResponses).collect(Collectors.toList());
    }

    @GetMapping("/all")
    public Collection<ItemRequestWithResponsesDto> getAllOfOthersInPages(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                                                         @Valid @Min(0) @RequestParam(required = false) Integer from,
                                                                         @Valid @Min(1) @RequestParam(required = false) Integer size) {
        return itemRequestService.getAllOfOthersInPages(userId, from, size)
                .stream().map(itemRequestMapper::toDtoWithResponses).collect(Collectors.toList());
    }
}