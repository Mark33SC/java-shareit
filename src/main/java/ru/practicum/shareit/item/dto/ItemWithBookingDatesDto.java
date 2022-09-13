package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.booking.dto.BookingBookerIdDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode
public class ItemWithBookingDatesDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingBookerIdDto lastBooking;
    private BookingBookerIdDto nextBooking;
    private List<Comment> comments;

    public void setLastBooking(ru.practicum.shareit.booking.Booking lastBooking) {
        this.lastBooking = new BookingBookerIdDto(lastBooking.getId(), lastBooking.getBooker().getId());
    }

    public void setNextBooking(ru.practicum.shareit.booking.Booking nextBooking) {
        this.nextBooking = new BookingBookerIdDto(nextBooking.getId(), nextBooking.getBooker().getId());
    }

    @Data
    @Builder
    public static class Comment {

        private Long id;

        private String text;

        private String authorName;

        private LocalDateTime created;
    }
}