package ru.practicum.ewm.hit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointsHitDto;
import ru.practicum.dto.StatsResponseDto;
import ru.practicum.ewm.hit.mapper.EndpointsHitMapper;
import ru.practicum.ewm.hit.mapper.StatsResponseMapper;
import ru.practicum.ewm.hit.model.StatsResponse;
import ru.practicum.ewm.hit.storage.EndpointsHitStorage;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EndpointsHitService {
    private final EndpointsHitStorage storage;
    private final EndpointsHitMapper mapper;
    private final StatsResponseMapper responseMapper;

    @Transactional
    public EndpointsHitDto save(EndpointsHitDto hit) {
        return mapper.toDto(storage.save(mapper.toEntity(hit)));
    }

    @Transactional(readOnly = true)
    public List<StatsResponseDto> getStats(LocalDateTime start, LocalDateTime end, Collection<String> uris, Boolean unique) {
        List<StatsResponse> stats;
        if (uris.isEmpty()) stats = unique ? storage.getUniqIpStats(start, end) : storage.getAllStats(start, end);
        else stats = unique ? storage.getUniqIpStats(start, end, uris) : storage.getAllStats(start, end, uris);
        return stats.stream().map(responseMapper::toDto).collect(Collectors.toList());
    }
}