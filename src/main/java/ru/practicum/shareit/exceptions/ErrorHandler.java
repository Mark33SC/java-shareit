package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice(basePackages = {"ru.practicum.shareit.item",
        "ru.practicum.shareit.user",
        "ru.practicum.shareit.requests"})
public class ErrorHandler {
    @ExceptionHandler({ItemNotFoundException.class,
            UserNotFoundException.class,
            RequestNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(final Exception e) {
        return Map.of("error: ", "object not found");
    }
}