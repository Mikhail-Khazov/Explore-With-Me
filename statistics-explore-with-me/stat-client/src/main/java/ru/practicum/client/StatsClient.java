package ru.practicum.client;

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
public class StatsClient {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String URL = "http://localhost:9090";

    public ResponseEntity<EndpointsHitDto> saveStatistic(EndpointsHitDto endpointsHitDto) {
        return restTemplate.postForEntity(URL + "/hit", endpointsHitDto, EndpointsHitDto.class);
    }

    public ResponseEntity<StatsResponseDto[]> getStatistic(LocalDateTime start,
                                                           LocalDateTime end,
                                                           Collection<String> uris,
                                                           Boolean unique) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Map<String, Object> param = new HashMap<>();
        param.put("start", start.format(formatter));
        param.put("end", end.format(formatter));
        param.put("unique", unique.toString());
        param.put("uris", String.join(",", uris));

        return restTemplate.getForEntity(URL + "/stats?start={start}&end={end}&unique={unique}&uris={uris}",
                StatsResponseDto[].class, param);
    }
}
