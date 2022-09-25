package ru.practicum.shareit.requests.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.item.Item;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemRequestWithResponsesDto {
    @EqualsAndHashCode.Include
    private Long id;
    @NotBlank
    @Size(max = 500)
    private String description;
    private LocalDateTime created;

    List<Item> items;
}
