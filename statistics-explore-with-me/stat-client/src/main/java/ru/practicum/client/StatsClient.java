package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.EndpointsHitDto;
import ru.practicum.dto.StatsResponseDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class StatsClient {
    private final RestTemplate restTemplate;
    @Value("${stats-service.url}")
    private String url;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ResponseEntity<EndpointsHitDto> saveStatistic(EndpointsHitDto endpointsHitDto) {
        return restTemplate.postForEntity(url + "/hit", endpointsHitDto, EndpointsHitDto.class);
    }

    public ResponseEntity<StatsResponseDto[]> getStatistic(LocalDateTime start,
                                                           LocalDateTime end,
                                                           Collection<String> uris,
                                                           Boolean unique) {

        Map<String, Object> param = new HashMap<>();
        param.put("start", start.format(formatter));
        param.put("end", end.format(formatter));
        param.put("uris", uris.isEmpty() ? null : String.join(",", uris));
        param.put("unique", unique != null && unique);

        return restTemplate.getForEntity(url + "/stats?start={start}&end={end}&unique={unique}&uris={uris}",
                StatsResponseDto[].class, param);
    }
}
