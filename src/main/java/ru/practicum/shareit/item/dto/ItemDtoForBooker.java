package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.dto.UserDtoForBooker;

@Data
@Builder
public class ItemDtoForBooker {
    Long id;
    String name;
    String description;
    UserDtoForBooker owner;
}