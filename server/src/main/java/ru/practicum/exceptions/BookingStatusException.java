package ru.practicum.exceptions;

public class BookingStatusException extends RuntimeException {
    public BookingStatusException(String message) {
        super(message);
    }
}