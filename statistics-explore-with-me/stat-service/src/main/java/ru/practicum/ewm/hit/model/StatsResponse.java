package ru.practicum.ewm.hit.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class StatsResponse {
    private String service;
    @Id
    private String uri;
    private Long hits;
}
