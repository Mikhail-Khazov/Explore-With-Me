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
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.EnterParams;
import ru.practicum.event.model.Event;
import ru.practicum.event.storage.CustomEventRepository;
import ru.practicum.event.storage.EventStorage;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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

//        event.setViews(getViews(event));

        return mapper.toFullDto(event);
    }

    public List<EventFullDto> getByParam(EnterParams params, PageRequest pageRequest, HttpServletRequest request) {
        Specification<Event> spec = customRepository.createSpecification(params);
        saveStatistic(request);
        List<Event> events = storage.findAll(spec, pageRequest).getContent();
//        events.forEach(e -> e.setViews(getViews(e)));                               //TODO n+1
        return events.stream().map(mapper::toFullDto).collect(Collectors.toList());
    }

    private Long getViews(Event event) {
        Long views;
        StatsResponseDto[] response = statsClient.getStatistic(event.getPublishedOn(), LocalDateTime.now(), List.of(event.getId().toString()), false).getBody();
        if (response != null) views = Arrays.stream(response).findFirst().orElseThrow(EventNotFoundException::new).getHits(); //TODO EXCEPTION
        else views = 0L;
        return views;
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
