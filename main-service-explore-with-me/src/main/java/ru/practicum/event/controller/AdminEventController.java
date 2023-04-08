package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.common.enums.EventState;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventDto;
import ru.practicum.event.model.EnterParams;
import ru.practicum.event.service.AdminEventService;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.common.Utils.*;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
public class AdminEventController {
    private final AdminEventService service;

    @GetMapping
    public List<EventFullDto> getByParams(@RequestParam(required = false) List<Long> users,
                                          @RequestParam(required = false) List<EventState> states,
                                          @RequestParam(required = false) List<Long> categories,
                                          @RequestParam(required = false) @DateTimeFormat(pattern = DT_PATTERN) LocalDateTime rangeStart,
                                          @RequestParam(required = false) @DateTimeFormat(pattern = DT_PATTERN) LocalDateTime rangeEnd,
                                          @RequestParam(defaultValue = DEFAULT_FROM_VALUE) @Min(0) int from,
                                          @RequestParam(defaultValue = DEFAULT_SIZE_VALUE) @Min(1) @Max(30) int size){
        EnterParams params = EnterParams.builder()
                .users(users)
                .states(states)
                .categories(categories)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .build();
        return service.getByParams(params, PageRequest.of(from / size, size));
    }


    @PatchMapping(path = "/{eventId}")
    public EventFullDto update(@PathVariable long eventId, @Validated @RequestBody UpdateEventDto updateDto){
        return service.update(eventId, updateDto);
    }

}
