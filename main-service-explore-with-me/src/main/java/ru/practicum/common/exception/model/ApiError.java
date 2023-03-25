package ru.practicum.common.exception.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ApiError {
    List<Throwable> errors;
    String message;
    String reason;
    HttpStatus status;
    LocalDateTime timestamp;
}

