package ru.practicum.shareit.exceptions;

public class UserIllegalArgumentException extends Exception {
    public UserIllegalArgumentException() {
    }

    public UserIllegalArgumentException(String message) {
        super(message);
    }
}
