package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

import static ru.practicum.common.Utils.TITLE_MAX_LENGTH;
import static ru.practicum.common.Utils.TITLE_MIN_LENGTH;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {
    private List<Long> events;
    private Boolean pinned;
    @NotBlank(message = "Enter a compilation title")
    @Size(min = TITLE_MIN_LENGTH, max = TITLE_MAX_LENGTH, message = "Category title length between 3 and 120")
    private String title;
}
