package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.common.exception.CompilationNotFoundException;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationDto;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.storage.CompilationStorage;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.AdminEventService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminCompilationService {
    private final CompilationStorage storage;
    private final CompilationMapper mapper;
    private final AdminEventService eventService;

    public CompilationDto create(NewCompilationDto newCompilationDto) {
        List<Event> events = eventService.getAllForCompilation(newCompilationDto.getEvents());
        if (newCompilationDto.getEvents().size() != events.size())
            throw new CompilationNotFoundException("Incorrect event Id values");
        Compilation compilation = mapper.toModel(newCompilationDto, events);
        return mapper.toDto(storage.save(compilation));
    }

    public CompilationDto update(long compId, UpdateCompilationDto updateDto) {
        Compilation compilation = storage.findById(compId).orElseThrow(CompilationNotFoundException::new);
        if (null != updateDto.getEvents()) {
            if (!updateDto.getEvents().isEmpty()) {
                List<Event> events = eventService.getAllForCompilation(updateDto.getEvents());
                compilation.setEvents(events);
            } else compilation.setEvents(Collections.emptyList());
        }
        if (null != updateDto.getPinned()) compilation.setPinned(updateDto.getPinned());
        if (null != updateDto.getTitle()) compilation.setTitle(updateDto.getTitle());
        return mapper.toDto(compilation);
    }

    public void delete(long compId) {
        Compilation compilation = storage.findById(compId).orElseThrow(CompilationNotFoundException::new);
        storage.delete(compilation);
    }
}
