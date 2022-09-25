package ru.practicum.shareit.request;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestCreateDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;
    private static final String header = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> createRequest(
            @RequestHeader(header) long requesterId,
            @RequestBody @Valid ItemRequestCreateDto itemRequestCreateDto) {
        return itemRequestClient.create(requesterId, itemRequestCreateDto);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequestsOfUser(
            @RequestHeader(header) long requesterId) {
        return itemRequestClient.getItemRequestsOfUser(requesterId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getItemRequestsOfOther(
            @RequestHeader(header) long userId,
            @Valid @PositiveOrZero @RequestParam(required = false) Integer from,
            @Valid @Positive @RequestParam(required = false) Integer size) {
        return itemRequestClient.getItemRequestsOfOther(userId, from, size);
    }

    @GetMapping("/{itemRequestId}")
    public ResponseEntity<Object> getItemRequestById(
            @RequestHeader(header) long userId,
            @PathVariable long itemRequestId) {
        return itemRequestClient.getItemRequestById(userId, itemRequestId);

    }

}
