package ru.practicum.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDtoForBooker {
    private Long id;
    private String name;
}