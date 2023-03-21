package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EndpointsHitDto {
    private Long id;
    @NotBlank(message = "field app can not be blank")
    private String app;
    @NotBlank(message = "field uri can not be blank")
    private String uri;
    private String ip;
    @Future(message = "the event must be start in the future")
    @NotNull(message = "field timestamp can not be blank")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
