package ru.practicum.ewm.hit.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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

@Getter
@Setter
@RequiredArgsConstructor
@Service
public class EndpointsHitService {
    private final EndpointsHitStorage storage;
    private final EndpointsHitMapper mapper;
    private final StatsResponseMapper responseMapper;

    @Transactional
    public EndpointsHitDto save(EndpointsHitDto hit, String ip) {
        return mapper.toDto(storage.save(mapper.toEntity(hit, ip)));
    }

    @Transactional(readOnly = true)
    public List<StatsResponseDto> getStats(LocalDateTime start, LocalDateTime end, Collection<String> uris, Boolean unique) {
        List<StatsResponse> stats;
        stats = unique ? storage.getUniqIpStats(start, end, uris) : storage.getAllStats(start, end, uris);
        return stats.stream().map(responseMapper::toDto).collect(Collectors.toList());
    }
}