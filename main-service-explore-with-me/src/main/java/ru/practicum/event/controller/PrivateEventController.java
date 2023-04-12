package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.*;
import ru.practicum.event.service.PrivateEventService;
import ru.practicum.request.dto.ParticipationRequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.common.Utils.DEFAULT_FROM_VALUE;
import static ru.practicum.common.Utils.DEFAULT_SIZE_VALUE;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Validated
public class PrivateEventController {
    private final PrivateEventService service;

    @GetMapping
    public List<EventFullDto> getByUserId(@PathVariable Long userId,
                                          @RequestParam(defaultValue = DEFAULT_FROM_VALUE) @PositiveOrZero int from,
                                          @RequestParam(defaultValue = DEFAULT_SIZE_VALUE) @Positive int size) {
        return service.getByUserId(userId, PageRequest.of(from / size, size));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public EventFullDto create(@PathVariable Long userId, @Validated @RequestBody NewEventDto newEventDto) {
        return service.create(userId, newEventDto);
    }

    @GetMapping(path = "/{eventId}")
    public EventFullDto get(@PathVariable Long userId, @PathVariable Long eventId) {
        return service.get(userId, eventId);
    }

    @PatchMapping(path = "/{eventId}")
    public EventFullDto update(@PathVariable Long userId, @PathVariable Long eventId, @Validated @RequestBody UpdateEventPrivateRequest dto) {
        return service.update(userId, eventId, dto);
    }

    @GetMapping(path = "/{eventId}/requests")
    public List<ParticipationRequestDto> getEventRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        return service.getEventRequests(userId, eventId);
    }

    @PatchMapping(path = "/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequestsStatus(@PathVariable Long userId,
                                                               @PathVariable Long eventId,
                                                               @Validated @RequestBody EventRequestStatusUpdateRequest updateStatus) {
        return service.updateRequestsStatus(userId, eventId, updateStatus);
    }
}
