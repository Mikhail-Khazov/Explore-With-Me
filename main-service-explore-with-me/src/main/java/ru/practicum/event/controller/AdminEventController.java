package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.common.enums.EventState;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.model.EnterParams;
import ru.practicum.event.service.AdminEventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.common.Utils.DEFAULT_FROM_VALUE;
import static ru.practicum.common.Utils.DEFAULT_SIZE_VALUE;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Validated
public class AdminEventController {
    private final AdminEventService service;

    @GetMapping
    public List<EventFullDto> getByParams(@RequestParam(required = false) List<Long> users,
                                          @RequestParam(required = false) List<EventState> states,
                                          @RequestParam(required = false) List<Long> categories,
                                          @RequestParam(required = false) LocalDateTime rangeStart,
                                          @RequestParam(required = false) LocalDateTime rangeEnd,
                                          @RequestParam(defaultValue = DEFAULT_FROM_VALUE) @PositiveOrZero int from,
                                          @RequestParam(defaultValue = DEFAULT_SIZE_VALUE) @Positive int size) {
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
    public EventFullDto update(@PathVariable long eventId, @Validated @RequestBody UpdateEventAdminRequest updateDto) {
        return service.update(eventId, updateDto);
    }

}
