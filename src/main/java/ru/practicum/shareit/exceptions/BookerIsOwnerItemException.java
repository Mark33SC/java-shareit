package ru.practicum.shareit.exceptions;

public class BookerIsOwnerItemException extends RuntimeException {
    public BookerIsOwnerItemException() {
    }

    public BookerIsOwnerItemException(String message) {
        super(message);
    }
}
