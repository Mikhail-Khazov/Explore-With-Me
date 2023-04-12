package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.model.Category;
import ru.practicum.category.service.AdminCategoryService;
import ru.practicum.common.enums.EventState;
import ru.practicum.common.enums.RequestStatus;
import ru.practicum.common.exception.EventCreateException;
import ru.practicum.common.exception.EventNotFoundException;
import ru.practicum.common.exception.EventUpdateException;
import ru.practicum.common.exception.RequestException;
import ru.practicum.event.dto.*;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.storage.CustomEventRepository;
import ru.practicum.event.storage.EventStorage;
import ru.practicum.location.model.Location;
import ru.practicum.location.service.LocationService;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.storage.RequestStorage;
import ru.practicum.user.model.User;
import ru.practicum.user.service.AdminUserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PrivateEventService {
    private final EventStorage storage;
    private final EventMapper mapper;
    private final AdminUserService userService;
    private final AdminCategoryService categoryService;
    private final CustomEventRepository customEventRepository;
    private final RequestStorage requestStorage;
    private final RequestMapper requestMapper;
    private final LocationService locationService;

    @Transactional(readOnly = true)
    public List<EventFullDto> getByUserId(Long userId, PageRequest pageRequest) {
        userService.getById(userId);
        return storage.findByInitiatorId(userId, pageRequest).stream().map(mapper::toFullDto).collect(Collectors.toList());
    }

    public EventFullDto create(Long userId, NewEventDto newEventDto) {
        checkStartTime(newEventDto.getEventDate());

        User user = userService.getById(userId);
        Category category = categoryService.getById(newEventDto.getCategory());
        Location location = locationService.get(newEventDto.getLocation());
        Event event = mapper.toModel(newEventDto, category, user, location);

        return mapper.toFullDto(storage.save(event));
    }

    @Transactional(readOnly = true)
    public EventFullDto get(Long userId, Long eventId) {
        userService.getById(userId);
        Event event = getByEventAndUserIds(eventId, userId);
        return mapper.toFullDto(event);
    }


    public EventFullDto update(Long userId, Long eventId, UpdateEventPrivateRequest dto) {
        checkStartTime(dto.getEventDate());
        userService.getById(userId);
        Event event = getByEventAndUserIds(eventId, userId);
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new EventUpdateException("Event already published");
        }

        customEventRepository.updateEventFields(event, dto);

        switch (dto.getStateAction()) {
            case SEND_TO_REVIEW:
                event.setState(EventState.PENDING);
                break;
            case CANCEL_REVIEW:
                event.setState(EventState.CANCELED);
                break;
            default:
                throw new EventUpdateException("Unknown stateAction");
        }

        return mapper.toFullDto(event);
    }

    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        getByEventAndUserIds(eventId, userId);
        return requestStorage.findAllByEventId(eventId).stream().map(requestMapper::toDto).collect(Collectors.toList());
    }

    public EventRequestStatusUpdateResult updateRequestsStatus(Long userId,
                                                               Long eventId,
                                                               EventRequestStatusUpdateRequest updateStatus) {
        if (updateStatus == null) throw new RequestException("updateStatus is null");
        Event event = getByEventAndUserIds(eventId, userId);
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0 ||
                updateStatus.getRequestIds().isEmpty())
            return new EventRequestStatusUpdateResult(Collections.emptyList(), Collections.emptyList());
        if (event.getParticipantLimit() <= requestStorage.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED) && event.getParticipantLimit() != 0)
            throw new RequestException("Participants limit reached");

        List<Request> requests = requestStorage.findAllByIdIn(updateStatus.getRequestIds());
        if (requests.size() != updateStatus.getRequestIds().size())
            throw new RequestException("Incorrect requests list");
        if (requests.stream().anyMatch(r -> !r.getStatus().equals(RequestStatus.PENDING)))
            throw new RequestException("Request status must be PENDING");

        if (updateStatus.getStatus().equals(RequestStatus.REJECTED)) {
            requests.forEach(r -> r.setStatus(RequestStatus.REJECTED));
            return new EventRequestStatusUpdateResult(Collections.emptyList(), requests.stream().map(requestMapper::toDto).collect(Collectors.toList()));
        }
        int participantsLimit = event.getParticipantLimit();
        List<Request> confirmedRequests = new ArrayList<>();
        List<Request> rejectedRequests = new ArrayList<>();
        for (Request request : requests) {
            if (participantsLimit > 0) {
                request.setStatus(RequestStatus.CONFIRMED);
                confirmedRequests.add(request);
                participantsLimit--;
            } else {
                request.setStatus(RequestStatus.REJECTED);
                rejectedRequests.add(request);
            }
        }
        return new EventRequestStatusUpdateResult(confirmedRequests.stream().map(requestMapper::toDto).collect(Collectors.toList()),
                rejectedRequests.stream().map(requestMapper::toDto).collect(Collectors.toList()));
    }

    private static void checkStartTime(LocalDateTime eventDate) {
        if (eventDate != null && eventDate.minusHours(2).isBefore(LocalDateTime.now()))
            throw new EventCreateException("Incorrect start time");
    }

    private Event getByEventAndUserIds(Long eventId, Long userId) {
        return storage.findByIdAndInitiatorId(eventId, userId).orElseThrow(EventNotFoundException::new);
    }


}
