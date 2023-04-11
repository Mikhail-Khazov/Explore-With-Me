package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.location.dto.LocationDto;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.common.Utils.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventDto {
    @Size(max = ANNOTATION_MAX_LENGTH, min = ANNOTATION_MIN_LENGTH, message = "Not less than 20 and not more than 2000")
    private String annotation;
    private Long category;
    @Size(max = DESCRIPTION_MAX_LENGTH, min = DESCRIPTION_MIN_LENGTH, message = "Not less than 20 and not more than 7000")
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DT_PATTERN)
    private LocalDateTime eventDate;
    private LocationDto location;
    private Boolean paid;
    @PositiveOrZero
    private Integer participantLimit;
    private Boolean requestModeration;
    @Size(max = TITLE_MAX_LENGTH, min = TITLE_MIN_LENGTH, message = "Not less than 3 and not more than 120")
    private String title;
}
