package ru.practicum.shareit.exceptions;

public class ItemIllegalArgumentException extends RuntimeException {
    public ItemIllegalArgumentException() {
    }

    public ItemIllegalArgumentException(String message) {
        super(message);
    }
}
