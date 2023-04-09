package ru.practicum.common.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiError {
    private String status;
    @JsonProperty("message")
    private String message;
    private String timestamp;
}

