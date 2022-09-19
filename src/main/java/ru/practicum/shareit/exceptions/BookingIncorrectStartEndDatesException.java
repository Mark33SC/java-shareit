package ru.practicum.shareit.exceptions;

public class BookingIncorrectStartEndDatesException extends RuntimeException {

    public BookingIncorrectStartEndDatesException(String message) {
        super(message);
    }
}
