package ru.practicum.exceptions;

public class ItemRequestNotFoundException extends RuntimeException {
    public ItemRequestNotFoundException() {
    }

    public ItemRequestNotFoundException(String message) {
        super(message);
    }
}
