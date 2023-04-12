package ru.practicum.common.exception;

public class SortParamNotFoundException extends RuntimeException {
    public SortParamNotFoundException() {
        super();
    }

    public SortParamNotFoundException(String message) {
        super(message);
    }

    public SortParamNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
