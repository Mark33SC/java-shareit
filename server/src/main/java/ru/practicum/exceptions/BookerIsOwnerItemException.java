package ru.practicum.exceptions;

public class BookerIsOwnerItemException extends RuntimeException {

    public BookerIsOwnerItemException(String message) {
        super(message);
    }
}
