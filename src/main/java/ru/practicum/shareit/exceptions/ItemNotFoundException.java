package ru.practicum.shareit.exceptions;

public class ItemNotFoundException extends Exception {
    public ItemNotFoundException() {
    }

    public ItemNotFoundException(String message) {
        super(message);
    }
}
