package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.common.enums.EventState;
import ru.practicum.common.exception.EventNotFoundException;
import ru.practicum.common.exception.EventUpdateException;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.EnterParams;
import ru.practicum.event.model.Event;
import ru.practicum.event.storage.CustomEventRepository;
import ru.practicum.event.storage.EventStorage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminEventService {
    private final EventStorage storage;
    private final EventMapper mapper;
    private final CustomEventRepository customRepository;

    public Event getByIdForComment(Long eventId){
        return storage.findByIdAndStateEquals(eventId, EventState.PUBLISHED).
                orElseThrow(() -> new NotFoundException(String.format("Event with ID %d not found", eventId)));
    }
    public List<Event> getAllForCompilation(List<Long> events) {
        return storage.findAllById(events);
    }

    @Transactional
    public EventFullDto update(long eventId, UpdateEventAdminRequest dto) {
        if (null != dto.getEventDate() && dto.getEventDate().isBefore(LocalDateTime.now().plusHours(1)))
            throw new EventUpdateException("Event can be edited no later than an hour before the start");
        Event event = storage.findById(eventId).orElseThrow(EventNotFoundException::new);
        if (event.getState() != EventState.PENDING) throw new EventUpdateException("Event state must be PENDING");
        switch (dto.getStateAction()) {
            case PUBLISH_EVENT:
                event.setState(EventState.PUBLISHED);
                break;
            case REJECT_EVENT:
                event.setState(EventState.CANCELED);
                break;
            default:
                throw new EventUpdateException("Unknown stateAction");
        }

        customRepository.updateEventFields(event, dto);

        return mapper.toFullDto(event);
    }

    public List<EventFullDto> getByParams(EnterParams params, PageRequest pageRequest) {
        Specification<Event> spec = customRepository.createSpecification(params);
        return storage.findAll(spec, pageRequest).stream().map(mapper::toFullDto).collect(Collectors.toList());
    }
}
