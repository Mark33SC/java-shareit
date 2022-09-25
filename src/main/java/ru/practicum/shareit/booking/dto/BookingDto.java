package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.item.dto.ItemDtoForBooker;
import ru.practicum.shareit.user.dto.UserDtoForBooker;

import java.time.LocalDateTime;

@Data
@Builder
@EqualsAndHashCode
public class BookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemDtoForBooker item;
    private UserDtoForBooker booker;
    private String status;
}