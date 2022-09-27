package ru.practicum.exceptions;

public class BookingIncorrectStartEndDatesException extends RuntimeException {

    public BookingIncorrectStartEndDatesException(String message) {
        super(message);
    }
}
