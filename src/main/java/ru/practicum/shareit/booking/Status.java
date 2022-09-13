package ru.practicum.shareit.booking;

import ru.practicum.shareit.exceptions.BookingStatusException;

public enum Status {
    CURRENT,
    COMPLETED,
    FUTURE,
    WAITING,
    APPROVED,
    REJECTED,
    CANCELED,
    PAST,
    ALL;

    public static Status findByName(String nameString) {
        for (Status status : values()) {
            if (status.name().equalsIgnoreCase(nameString)) {
                return status;
            }
        }
        throw new BookingStatusException(String.format("Unknown state: %s", nameString));
    }
}