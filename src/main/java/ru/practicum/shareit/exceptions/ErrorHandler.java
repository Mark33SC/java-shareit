package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({ItemNotFoundException.class,
            UserNotFoundException.class,
            UserNotOwnerItemException.class,
            RequestNotFoundException.class,
            BookingNotFoundException.class,
            BookerIsOwnerItemException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({BookingValidationException.class,
            MethodArgumentNotValidException.class,
            CommentValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({ItemIllegalArgumentException.class,
            BookingStatusException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse exceptionHandlerByBadeRequest(RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }
}