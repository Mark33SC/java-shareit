package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingCreateDto;

import java.util.List;

public interface BookingService {
    List<Booking> getAllBookings(long userId, String status, int from, int size);
    List<Booking> getAllBookingsForItemsOwner(long itemOwnerId, String status, int from, int size);
    Booking getBookingById(long bookingId, long userId);
    Booking addBooking(BookingCreateDto bookingCreateDto, long userId);
    Booking approveBooking(long bookingId, long userId, boolean isApproved);
}