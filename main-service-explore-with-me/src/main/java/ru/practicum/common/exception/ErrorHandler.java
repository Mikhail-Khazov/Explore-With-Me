package ru.practicum.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static ru.practicum.common.Utils.DT_PATTERN;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler({UserNotFoundException.class, CompilationNotFoundException.class, CategoryNotFoundException.class,
            SortParamNotFoundException.class, RequestNotFoundException.class, LocationNotFoundException.class})
    @ResponseStatus(NOT_FOUND)
    public ApiError handleNotFoundException(final RuntimeException e) {
        log.warn("404 {}", e.getMessage(), e);
        return new ApiError(NOT_FOUND.name(), e.getMessage(), getTimestamp());
    }

    @ExceptionHandler()
    @ResponseStatus(CONFLICT)
    public ApiError handleEventUpdateException(final EventUpdateException e) {
        log.warn("409 {}", e.getMessage(), e);
        return new ApiError(CONFLICT.name(), e.getMessage(), getTimestamp());
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public ApiError handleAlreadyExistException(final EntityAlreadyExistException e) {
        log.warn("409 {}", e.getMessage(), e);
        return new ApiError(CONFLICT.name(), e.getMessage(), getTimestamp());
    }

    @ExceptionHandler(RequestException.class)
    @ResponseStatus(CONFLICT)
    public ApiError handleRequestException(final RequestException e) {
        log.warn("409 {}", e.getMessage(), e);
        return new ApiError(CONFLICT.name(), e.getMessage(), getTimestamp());
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public ApiError handleEventCreateException(final EventCreateException e) {
        log.warn("409 {}", e.getMessage(), e);
        return new ApiError(CONFLICT.name(), e.getMessage(), getTimestamp());
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public ApiError handleUserConflictException(final UserConflictException e) {
        log.warn("409 {}", e.getMessage(), e);
        return new ApiError(CONFLICT.name(), e.getMessage(), getTimestamp());
    }

    private String getTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DT_PATTERN));
    }
}


