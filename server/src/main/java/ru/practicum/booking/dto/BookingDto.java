package ru.practicum.booking.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.item.dto.ItemDtoForBooker;
import ru.practicum.user.dto.UserDtoForBooker;

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