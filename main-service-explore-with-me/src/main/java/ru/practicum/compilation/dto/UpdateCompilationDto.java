package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.List;

import static ru.practicum.common.Utils.TITLE_MAX_LENGTH;
import static ru.practicum.common.Utils.TITLE_MIN_LENGTH;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompilationDto {
    private List<Long> events;
    private Boolean pinned;
    @Size(min = TITLE_MIN_LENGTH, max = TITLE_MAX_LENGTH, message = "Category title length must be between 3 and 120")
    private String title;
}
