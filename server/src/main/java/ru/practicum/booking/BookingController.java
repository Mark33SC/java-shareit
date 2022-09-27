package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.booking.dto.BookingCreateDto;
import ru.practicum.booking.dto.BookingMapper;
import ru.practicum.booking.dto.BookingDto;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @GetMapping
    public List<BookingDto> getAll(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(defaultValue = "ALL") String state,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "100") Integer size
    ) {
        return bookingService.getAllBookings(userId, state, from, size).stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsForItemsOwner(
            @RequestHeader("X-Sharer-User-Id") long itemOwnerId,
            @RequestParam(defaultValue = "All") String state,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "100") Integer size
    ) {
        return bookingService.getAllBookingsForItemsOwner(itemOwnerId, state, from, size).stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public BookingDto getById(
            @PathVariable long id,
            @RequestHeader("X-Sharer-User-Id") long userId
    ) {
        return BookingMapper.toBookingDto(
                bookingService.getBookingById(id, userId)
        );
    }

    @PostMapping
    public BookingDto addBooking(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestBody BookingCreateDto bookingCreateDto
    ) {
        return BookingMapper.toBookingDto(bookingService.addBooking(bookingCreateDto, userId));
    }

    @PatchMapping("/{id}")
    public BookingDto approveBooking(
            @PathVariable long id,
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam boolean approved
    ) {
        return BookingMapper.toBookingDto(
                bookingService.approveBooking(id, userId, approved)
        );
    }
}