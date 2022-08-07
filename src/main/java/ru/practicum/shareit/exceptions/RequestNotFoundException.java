package ru.practicum.shareit.exceptions;

public class RequestNotFoundException extends Exception {
    public RequestNotFoundException() {
    }

    public RequestNotFoundException(String message) {
        super(message);
    }
}
