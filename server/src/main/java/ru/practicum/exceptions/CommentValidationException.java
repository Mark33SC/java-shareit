package ru.practicum.exceptions;

public class CommentValidationException extends RuntimeException {

    public CommentValidationException(String message) {
        super(message);
    }
}
