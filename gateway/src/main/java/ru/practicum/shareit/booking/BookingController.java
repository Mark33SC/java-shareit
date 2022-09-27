package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;
    private static final String header = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> createBooking(
            @RequestHeader(header) long userId,
            @RequestBody @NonNull @Valid BookingCreateDto bookingCreateDto) {
        log.info("Creating booking {}, userId={}", bookingCreateDto, userId);
        return bookingClient.createBooking(bookingCreateDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> confirmBooking(
            @RequestHeader(header) long userId,
            @PathVariable long bookingId,
            @RequestParam boolean approved) {
        return bookingClient.confirmBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(
            @RequestHeader(header) long userId,
            @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBookingById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllBookings(
            @RequestHeader(header) long userId,
            @RequestParam(name = "state", defaultValue = "all") String stateParam,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    ResponseEntity<Object> getAllOwnerBookings(
            @RequestHeader(header) long userId,
            @RequestParam(name = "state", defaultValue = "all") String stateParam,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getAllOwnerBookings(userId, state, from, size);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> errorHandler(IllegalArgumentException ex) {
        Map<String, String> resp = new HashMap<>();
        resp.put("error", ex.getMessage());
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);

    }


}
