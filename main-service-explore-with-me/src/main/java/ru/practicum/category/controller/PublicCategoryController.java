package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.PubicCategoryService;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

import static ru.practicum.common.Utils.DEFAULT_FROM_VALUE;
import static ru.practicum.common.Utils.DEFAULT_SIZE_VALUE;

@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
public class PublicCategoryController {
    private final PubicCategoryService service;

    @GetMapping(path = "/{catId}")
    public CategoryDto get(@PathVariable long catId) {
        return service.get(catId);
    }

    @GetMapping
    public List<CategoryDto> getAll(@RequestParam(defaultValue = DEFAULT_FROM_VALUE) @Min(0) int from,
                                    @RequestParam(defaultValue = DEFAULT_SIZE_VALUE) @Min(1) @Max(30) int size){
        return service.getAll(PageRequest.of(from / size, size));
    }

}
