package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.event.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.common.Utils.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotBlank(message = "Annotation field must not be blank")
    @Size(max = ANNOTATION_MAX_LENGTH, min = ANNOTATION_MIN_LENGTH, message = "Not less than 20 and not more than 2000")
    private String annotation;
    @NotNull(message = "Category field must not be empty")
    private Long category;
    @NotBlank()
    @Size(max = DESCRIPTION_MAX_LENGTH, min = DESCRIPTION_MIN_LENGTH, message = "Not less than 20 and not more than 7000")
    private String description;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @NotNull(message = "Required field")
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    @NotBlank(message = "Title field must not be blank")
    @Size(max = TITLE_MAX_LENGTH, min = TITLE_MIN_LENGTH, message = "Not less than 3 and not more than 120")
    private String title;
}