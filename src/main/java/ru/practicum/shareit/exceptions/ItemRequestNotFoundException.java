package ru.practicum.shareit.exceptions;

public class ItemRequestNotFoundException extends RuntimeException {
    public ItemRequestNotFoundException() {
    }

    public ItemRequestNotFoundException(String message) {
        super(message);
    }
}
