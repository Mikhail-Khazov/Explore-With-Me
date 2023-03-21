package ru.practicum.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StatsResponseDto {
    private String service;
    private String uri;
    private Long hits;
}
