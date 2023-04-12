package ru.practicum.common.exception;

public class CompilationNotFoundException extends RuntimeException {
    public CompilationNotFoundException() {
        super();
    }

    public CompilationNotFoundException(String message) {
        super(message);
    }

    public CompilationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
