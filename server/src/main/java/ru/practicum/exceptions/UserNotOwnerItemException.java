package ru.practicum.exceptions;

public class UserNotOwnerItemException extends RuntimeException {
    public UserNotOwnerItemException() {
    }

    public UserNotOwnerItemException(String message) {
        super(message);
    }
}
