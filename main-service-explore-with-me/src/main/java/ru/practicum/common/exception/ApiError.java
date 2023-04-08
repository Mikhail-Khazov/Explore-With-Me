package ru.practicum.common.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiError {
//    private List<Throwable> errors; TODO
    private String status;
    private String reason;
    @JsonProperty("message")
    private String message;
    private String timestamp;
}

