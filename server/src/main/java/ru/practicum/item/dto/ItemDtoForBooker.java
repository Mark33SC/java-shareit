package ru.practicum.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.user.dto.UserDtoForBooker;

@Data
@Builder
public class ItemDtoForBooker {
    private Long id;
    private String name;
    private String description;
    private UserDtoForBooker owner;
}