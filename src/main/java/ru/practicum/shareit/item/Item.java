package ru.practicum.shareit.item;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.NonNull;
import ru.practicum.shareit.requests.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Item {
    @EqualsAndHashCode.Include
    private long id;
    @NotBlank
    @Size(max = 50)
    private String name;
    @NonNull
    @Size(max = 500)
    private String description;
    private final long ownerId;
    @NonNull
    private Boolean available;
    private ItemRequest request;
    private List<Long> bookingsId;
}
