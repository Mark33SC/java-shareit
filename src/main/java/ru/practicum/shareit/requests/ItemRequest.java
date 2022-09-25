package ru.practicum.shareit.requests;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemRequest {
    @EqualsAndHashCode.Include
    private long id;
    @Size(max = 500)
    private String description;
    private User requester;
    private LocalDateTime created;
}