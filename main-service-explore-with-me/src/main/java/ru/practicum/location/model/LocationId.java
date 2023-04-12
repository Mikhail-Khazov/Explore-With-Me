package ru.practicum.location.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class LocationId implements Serializable {
    private Float lat;
    private Float lon;
}
