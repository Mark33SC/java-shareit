package ru.practicum.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.user.dto.UserDtoForBooker;

@Data
@Builder
public class ItemDtoForBooker {
    Long id;
    String name;
    String description;
    UserDtoForBooker owner;
}