package ru.practicum.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ApiError handleInternalServerError(final Throwable e) {
        log.warn("500 {}", e.getMessage(), e);
        return new ApiError(INTERNAL_SERVER_ERROR.toString(), e.getCause().toString(), e.getMessage(), getTimestamp());
    }
//    @ExceptionHandler
//    @ResponseStatus(NOT_FOUND)
//    public ApiError handleInternalServerError(final CategoryNotFoundException e) {
//        log.warn("404 {}", e.getMessage(), e);
//        return new ApiError(NOT_FOUND.toString(), e.getCause().toString(), e.getMessage(), getTimestamp());
//    }
//
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ErrorResponse handleInternalServerError(final CompilationNotFoundException e) {
//        log.warn("404 {}", e.getMessage(), e);
//        return new ErrorResponse(e.getMessage());
//    }
//TODO

    @ExceptionHandler({UserNotFoundException.class, CompilationNotFoundException.class, CategoryNotFoundException.class,
            SortParamNotFoundException.class, RequestNotFoundException.class, LocationNotFoundException.class})
    @ResponseStatus(NOT_FOUND)
    public ApiError handleNotFoundException(final RuntimeException e) {
        log.warn("404 {}", e.getMessage(), e);
        return new ApiError(NOT_FOUND.toString(), e.getCause().toString(), e.getMessage(), getTimestamp());
    }

    @ExceptionHandler()
    @ResponseStatus(CONFLICT)
    public ApiError handleEventUpdateException(final EventUpdateException e) {
        log.warn("409 {}", e.getMessage(), e);
        return new ApiError(CONFLICT.toString(), e.getCause().toString(), e.getMessage(), getTimestamp());
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public ApiError handleAlreadyExistException(final EntityAlreadyExistException e) {
        log.warn("409 {}", e.getMessage(), e);
        return new ApiError(CONFLICT.toString(), e.getCause().toString(), e.getMessage(), getTimestamp());
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public ApiError handleRequestException(final RequestException e) {
        log.warn("409 {}", e.getMessage(), e);
        return new ApiError(CONFLICT.toString(), e.getCause().toString(), e.getMessage(), getTimestamp());
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public ApiError handleEventCreateException(final EventCreateException e) {
        log.warn("409 {}", e.getMessage(), e);
        return new ApiError(CONFLICT.toString(), e.getCause().toString(), e.getMessage(), getTimestamp());
    }

    private String getTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}


