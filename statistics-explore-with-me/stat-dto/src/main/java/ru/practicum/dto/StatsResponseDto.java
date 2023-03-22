package ru.practicum.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StatsResponseDto {
    private String app;
    private String uri;
    private Long hits;
}
