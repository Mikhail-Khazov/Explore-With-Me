package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.common.enums.EventState;
import ru.practicum.common.enums.RequestStatus;
import ru.practicum.common.exception.RequestException;
import ru.practicum.common.exception.RequestNotFoundException;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.PublicEventService;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.storage.RequestStorage;
import ru.practicum.user.model.User;
import ru.practicum.user.service.AdminUserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PrivateRequestService {
    private final RequestStorage storage;
    private final RequestMapper mapper;
    private final PublicEventService eventService;
    private final AdminUserService userService;

    public ParticipationRequestDto create(Long userId, Long eventId) {
        Optional<Request> existingRequest = storage.findByUserIdAndEventId(userId, eventId);
        Event event = eventService.getById(eventId);
        User user = userService.getById(userId);

        if (existingRequest.isPresent()) throw new RequestException("Request already exist");
        if (event.getInitiator().getId().equals(userId)) throw new RequestException("Request to own event");
        if (!event.getState().equals(EventState.PUBLISHED)) throw new RequestException("Event not published");
        if (event.getParticipantLimit() == event.getConfirmedRequests().size())
            throw new RequestException("Participants limit reached");

        Request request = Request.builder()
                .created(LocalDateTime.now())
                .event(event)
                .user(user)
                .build();

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) request.setStatus(RequestStatus.CONFIRMED);
        else  request.setStatus(RequestStatus.PENDING);

        return mapper.toDto(storage.save(request));
    }

    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> get(Long userId) {
        userService.getById(userId);
        return storage.findAllByUserId(userId).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public ParticipationRequestDto revocation(Long userId, Long requestId) {
        Request request = storage.findById(requestId).orElseThrow(RequestNotFoundException::new);
        userService.getById(userId);
        if (!Objects.equals(request.getUser().getId(), userId)) throw new RequestException("Incorrect request ID");
        request.setStatus(RequestStatus.CANCELED);
        return mapper.toDto(request);
    }
}
