package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.service.PublicCompilationsService;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

import static ru.practicum.common.Utils.DEFAULT_FROM_VALUE;
import static ru.practicum.common.Utils.DEFAULT_SIZE_VALUE;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/compilations")
public class PublicCompilationsController {
    private final PublicCompilationsService service;

    @GetMapping
    public List<CompilationDto> getAll(@RequestParam(required = false) Boolean pinned,
                                       @RequestParam(defaultValue = DEFAULT_FROM_VALUE) @Min(0) int from,
                                       @RequestParam(defaultValue = DEFAULT_SIZE_VALUE) @Min(1) @Max(30) int size) {
        return service.getAll(pinned, PageRequest.of(from / size, size));
    }

    @GetMapping(path = "/{compId}")
    public CompilationDto get(@PathVariable Long compId){
        return service.get(compId);
    }

    }
