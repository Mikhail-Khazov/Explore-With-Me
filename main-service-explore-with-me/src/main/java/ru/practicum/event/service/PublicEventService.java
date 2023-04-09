package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.StatsClient;
import ru.practicum.common.enums.EventState;
import ru.practicum.common.exception.EventNotFoundException;
import ru.practicum.dto.EndpointsHitDto;
import ru.practicum.dto.StatsResponseDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.EnterParams;
import ru.practicum.event.model.Event;
import ru.practicum.event.storage.CustomEventRepository;
import ru.practicum.event.storage.EventStorage;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PublicEventService {
    private final EventStorage storage;
    private final CustomEventRepository customRepository;
    private final StatsClient statsClient;
    private final EventMapper mapper;


    public EventFullDto get(long id, HttpServletRequest request) {
        Event event = storage.findByIdAndStateEquals(id, EventState.PUBLISHED).orElseThrow(EventNotFoundException::new);
        saveStatistic(request);
        event.setViews(getViews(event.getCreatedOn(), event.getEventDate(), List.of(request.getRequestURI())).stream().findFirst().orElseThrow().getHits());
        return mapper.toFullDto(event);
    }

    public List<EventShortDto> getByParam(EnterParams params, PageRequest pageRequest, HttpServletRequest request) {
        saveStatistic(request);
        Specification<Event> spec = customRepository.createSpecification(params);
        if (null != params.getText()) {
            spec = spec.and(customRepository.createSpecificationAnnotation(params));
            spec = spec.or(customRepository.createSpecificationDescription(params));
        }
        List<Event> events = storage.findAll(spec, pageRequest).getContent();
        List<String> uris = events.stream().map(e -> e.getId().toString()).collect(Collectors.toList());
        if (null == params.getRangeStart())
            params.setRangeStart(events.stream().min(Comparator.comparing(Event::getCreatedOn)).orElseThrow().getCreatedOn());
        if (null == params.getRangeEnd())
            params.setRangeEnd(LocalDateTime.now());

        Map<String, Long> views = getViews(params.getRangeStart(), params.getRangeEnd(), uris)
                .stream().collect(Collectors.toMap(StatsResponseDto::getUri, StatsResponseDto::getHits));
        events.forEach(e -> e.setViews(views.get(e.getId().toString())));
        return events.stream().map(mapper::toShortDto).collect(Collectors.toList());
    }

    private List<StatsResponseDto> getViews(LocalDateTime start, LocalDateTime end, List<String> uris) {
        return List.of(Objects.requireNonNull(statsClient.getStatistic(start, end, uris, false).getBody()));

    }

    private void saveStatistic(HttpServletRequest request) {
        EndpointsHitDto hit = EndpointsHitDto.builder()
                .app("ewm-main-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();
        statsClient.saveStatistic(hit);
    }

    public Event getById(Long eventId) {
        return storage.findById(eventId).orElseThrow(EventNotFoundException::new);
    }
}
