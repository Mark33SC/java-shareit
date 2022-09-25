package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookingBookerIdDto {
    private final Long id;
    private final Long bookerId;
}