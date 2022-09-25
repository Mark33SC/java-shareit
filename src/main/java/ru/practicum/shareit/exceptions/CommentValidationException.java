package ru.practicum.shareit.exceptions;

public class CommentValidationException extends RuntimeException {
    public CommentValidationException() {
    }

    public CommentValidationException(String message) {
        super(message);
    }
}
