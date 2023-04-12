package ru.practicum.common.exception;

public class EventUpdateException extends RuntimeException {
    public EventUpdateException() {
        super();
    }

    public EventUpdateException(String message) {
        super(message);
    }

    public EventUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
