package ru.practicum.ewm.hit.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.StatsResponseDto;
import ru.practicum.ewm.hit.model.StatsResponse;

@Component
public class StatsResponseMapper {
    public StatsResponseDto toDto(StatsResponse stats) {
        return StatsResponseDto.builder()
                .uri(stats.getUri())
                .service(stats.getService())
                .hits(stats.getHits())
                .build();
    }
}
