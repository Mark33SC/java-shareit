package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.requests.ItemRequest;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}