package main.server.advice;

import main.server.dao.Event;
import main.server.dto.ApiError;
import main.server.exception.ConflictException;
import main.server.exception.NotFoundException;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RestControllerAdvice(basePackages = "main.server")
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleExceptions(final RuntimeException e) {
        return new ApiError(new ArrayList<>(), e.getMessage(), "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.name(), LocalDateTime.now().format(Event.DATE_TIME_FORMATTER));
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleExceptions(final IllegalArgumentException e) {
        return new ApiError(new ArrayList<>(), e.getMessage(), "Incorrectly made request.", HttpStatus.BAD_REQUEST.name(), LocalDateTime.now().format(Event.DATE_TIME_FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleExceptions(final NotFoundException e) {
        return new ApiError(new ArrayList<>(), e.getMessage(), "The required object was not found.", HttpStatus.NOT_FOUND.name(), LocalDateTime.now().format(Event.DATE_TIME_FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleExceptions(final ConflictException e) {
        return new ApiError(new ArrayList<>(), e.getMessage(), "For the requested operation the conditions are not met.", HttpStatus.FORBIDDEN.name(), LocalDateTime.now().format(Event.DATE_TIME_FORMATTER));
    }
}
