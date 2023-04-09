package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.service.AdminCategoryService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
public class AdminCategoryController {
    private final AdminCategoryService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CategoryDto create(@RequestBody @Validated NewCategoryDto newCategoryDto) {
        return service.create(newCategoryDto);
    }

    @PatchMapping(path = "/{catId}")
    public CategoryDto update(@PathVariable long catId, @RequestBody @Validated NewCategoryDto categoryDto) {
        return service.update(catId, categoryDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{catId}")
    public void delete(@PathVariable long catId) {
        service.delete(catId);
    }


}
