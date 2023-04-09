package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.common.enums.EventSort;
import ru.practicum.common.enums.EventState;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.EnterParams;
import ru.practicum.event.service.PublicEventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.common.Utils.*;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
public class PublicEventController {
    private final PublicEventService service;

    @GetMapping(path = "/{id}")
    public EventFullDto get(@PathVariable long id, HttpServletRequest request) {
        return service.get(id, request);
    }

    @GetMapping
    public List<EventShortDto> getByParams(@RequestParam(required = false) String text,
                                           @RequestParam(required = false) List<Long> categories,
                                           @RequestParam(required = false) Boolean paid,
                                           @RequestParam(required = false) @DateTimeFormat(pattern = DT_PATTERN) LocalDateTime rangeStart,
                                           @RequestParam(required = false) @DateTimeFormat(pattern = DT_PATTERN) LocalDateTime rangeEnd,
                                           @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                           @RequestParam(required = false) String sort,
                                           @RequestParam(defaultValue = DEFAULT_FROM_VALUE) @Min(0) int from,
                                           @RequestParam(defaultValue = DEFAULT_SIZE_VALUE) @Min(1) @Max(30) int size) {

        EnterParams params = EnterParams.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .onlyAvailable(onlyAvailable)
                .states(List.of(EventState.PUBLISHED))
                .build();
        Sort sorting = EventSort.from(sort);

        return service.getByParam(params, PageRequest.of(from / size, size, sorting));
    }
}
