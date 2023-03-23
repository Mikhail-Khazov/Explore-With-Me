package ru.practicum.ewm.hit.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatsResponse {
    private String app;
    private String uri;
    private Long hits;
}
