package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationDto;
import ru.practicum.compilation.service.AdminCompilationService;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationController {
    private final AdminCompilationService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CompilationDto create(@RequestBody @Validated NewCompilationDto newCompilationDto){
        return service.create(newCompilationDto);
    }

    @PatchMapping(path = "/{compId}")
    public CompilationDto update(@PathVariable long compId, @RequestBody @Validated UpdateCompilationDto updateDto){
        return service.update(compId, updateDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{compId}")
    public void delete(@PathVariable long compId){
        service.delete(compId);
    }
}
