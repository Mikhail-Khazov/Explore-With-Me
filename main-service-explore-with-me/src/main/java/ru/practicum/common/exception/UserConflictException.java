package ru.practicum.common.exception;

public class UserConflictException extends RuntimeException {
    public UserConflictException() {
        super();
    }

    public UserConflictException(String message) {
        super(message);
    }
}
