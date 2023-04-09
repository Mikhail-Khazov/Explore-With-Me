package ru.practicum.ewm.hit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointsHitDto;
import ru.practicum.dto.StatsResponseDto;
import ru.practicum.ewm.hit.service.EndpointsHitService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class EndpointsHitController {
    private final EndpointsHitService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/hit")
    public EndpointsHitDto save(@Validated @RequestBody EndpointsHitDto hit) {
        return service.save(hit);
    }

    @GetMapping(path = "/stats")
    public List<StatsResponseDto> getStats(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam LocalDateTime start,
                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam LocalDateTime end,
                                           @RequestParam(defaultValue = "") Collection<String> uris,
                                           @RequestParam(defaultValue = "false") Boolean unique) {
        return service.getStats(start, end, uris, unique);
    }
}
