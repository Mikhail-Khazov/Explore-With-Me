package ru.practicum.ewm.hit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointsHitDto;
import ru.practicum.dto.StatsResponseDto;
import ru.practicum.ewm.hit.mapper.EndpointsHitMapper;
import ru.practicum.ewm.hit.storage.EndpointsHitStorage;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EndpointsHitService {
    private final EndpointsHitStorage storage;
    private final EndpointsHitMapper mapper;

    @Transactional
    public EndpointsHitDto save(EndpointsHitDto hit) {
        return mapper.toDto(storage.save(mapper.toEntity(hit)));
    }

    @Transactional(readOnly = true)
    public List<StatsResponseDto> getStats(LocalDateTime start, LocalDateTime end, Collection<String> uris, Boolean unique) {
        if (uris == null || uris.isEmpty()) {
            if (unique) {
                return storage.getUniqIpStats(start, end);
            } else {
                return storage.getAllStats(start, end);
            }
        } else {
            if (unique) {
                return storage.getUniqIpStats(start, end, uris);
            } else {
                return storage.getAllStats(start, end, uris);
            }
        }
    }
}