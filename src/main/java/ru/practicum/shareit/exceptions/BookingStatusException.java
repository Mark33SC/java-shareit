package ru.practicum.shareit.exceptions;

public class BookingStatusException extends RuntimeException {
    public BookingStatusException() {
    }

    public BookingStatusException(String message) {
        super(message);
    }
}