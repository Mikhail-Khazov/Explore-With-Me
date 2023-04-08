package ru.practicum.ewm.hit.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.EndpointsHitDto;
import ru.practicum.ewm.hit.model.EndpointsHit;

@Component
public class EndpointsHitMapper {
    public EndpointsHit toEntity(EndpointsHitDto hit) {
        return EndpointsHit.builder()
                .id(null)
                .app(hit.getApp())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .created(hit.getTimestamp())
                .build();
    }

    public EndpointsHitDto toDto(EndpointsHit hit) {
        return EndpointsHitDto.builder()
                .app(hit.getApp())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .timestamp(hit.getCreated())
                .build();
    }
}
