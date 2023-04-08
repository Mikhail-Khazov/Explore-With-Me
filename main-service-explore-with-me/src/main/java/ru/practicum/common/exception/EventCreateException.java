package ru.practicum.common.exception;

public class EventCreateException extends RuntimeException {
    public EventCreateException() {
        super();
    }

    public EventCreateException(String message) {
        super(message);
    }

    public EventCreateException(String message, Throwable cause) {
        super(message, cause);
    }
}
