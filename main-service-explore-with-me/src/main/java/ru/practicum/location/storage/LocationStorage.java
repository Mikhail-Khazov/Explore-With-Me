package ru.practicum.location.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.location.model.Location;

import java.util.Optional;

public interface LocationStorage extends JpaRepository<Location, Long> {
    Optional<Location> findByLatAndLon(Float lat, Float lon);
}
