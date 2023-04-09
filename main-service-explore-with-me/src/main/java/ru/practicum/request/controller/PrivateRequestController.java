package ru.practicum.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.service.PrivateRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class PrivateRequestController {
    private final PrivateRequestService service;

    @GetMapping
    public List<ParticipationRequestDto> get(@PathVariable Long userId) {
        return service.get(userId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ParticipationRequestDto create(@PathVariable Long userId, @RequestParam Long eventId) {
        return service.create(userId, eventId);
    }

    @PatchMapping(path = "/{requestId}/cancel")
    public ParticipationRequestDto revocation(@PathVariable Long userId, @PathVariable Long requestId) {
        return service.revocation(userId, requestId);
    }
}
