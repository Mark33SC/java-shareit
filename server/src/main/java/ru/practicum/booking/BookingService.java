package ru.practicum.booking;

import org.springframework.data.domain.Pageable;
import ru.practicum.booking.dto.BookingCreateDto;
import ru.practicum.user.User;

import java.util.List;

public interface BookingService {
    List<Booking> getAllBookings(long userId, String status, int from, int size);

    List<Booking> getAllBookingsForItemsOwner(long itemOwnerId, String status, int from, int size);

    Booking getBookingById(long bookingId, long userId);

    Booking addBooking(BookingCreateDto bookingCreateDto, long userId);

    Booking approveBooking(long bookingId, long userId, boolean isApproved);

    List<Booking> getAllByItemOwnerId(long itemOwnerId, Pageable page);

    List<Booking> getAllByBooker(User booker, Pageable page);
}