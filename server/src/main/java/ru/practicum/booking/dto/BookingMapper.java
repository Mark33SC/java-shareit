package ru.practicum.booking.dto;

import ru.practicum.booking.Booking;
import ru.practicum.booking.Status;
import ru.practicum.item.dto.ItemDtoForBooker;
import ru.practicum.user.dto.UserDtoForBooker;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {

        UserDtoForBooker itemOwner = UserDtoForBooker.builder()
                .id(booking.getItem().getOwner().getId())
                .name(booking.getItem().getOwner().getName())
                .build();

        ItemDtoForBooker item = ItemDtoForBooker.builder()
                .id(booking.getItem().getId())
                .name(booking.getItem().getName())
                .description(booking.getItem().getDescription())
                .owner(itemOwner)
                .build();

        UserDtoForBooker booker = UserDtoForBooker.builder()
                .id(booking.getBooker().getId())
                .name(booking.getBooker().getName())
                .build();

        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(item)
                .booker(booker)
                .status(booking.getStatus().toString())
                .build();
    }

    public static Booking toBooking(BookingCreateDto bookingCreateDto) {
        return new Booking(
                bookingCreateDto.getStart(), bookingCreateDto.getEnd(), Status.WAITING
        );
    }
}