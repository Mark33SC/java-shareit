package ru.practicum.shareit.exceptions;

public class BookingValidationException extends RuntimeException {
    public BookingValidationException() {
    }

    public BookingValidationException(String message) {
        super(message);
    }
}
