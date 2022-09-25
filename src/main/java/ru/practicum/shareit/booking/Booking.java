package ru.practicum.shareit.booking;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.NonNull;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDate;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Booking {
    @EqualsAndHashCode.Include
    private long id;
    @NonNull
    private LocalDate start;
    @NonNull
    private LocalDate end;
    private Item item;
    private User booker;
    private Status status;
}

