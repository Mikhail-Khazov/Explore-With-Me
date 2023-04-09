package ru.practicum.common.exception;

public class RequestNotFoundException extends RuntimeException {
    public RequestNotFoundException() {
        super();
    }

    public RequestNotFoundException(String message) {
        super(message);
    }

}
