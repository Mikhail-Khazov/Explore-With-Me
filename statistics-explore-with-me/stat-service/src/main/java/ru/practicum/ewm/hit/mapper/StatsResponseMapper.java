package ru.practicum.ewm.hit.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.StatsResponseDto;

@Component
public class StatsResponseMapper {
    public StatsResponseDto toDto(ru.practicum.ewm.hit.model.StatsResponse stats) {
        return StatsResponseDto.builder()
                .uri(stats.getUri())
                .app(stats.getApp())
                .hits(stats.getHits())
                .build();
    }
}
