package ru.practicum.location.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.mapper.LocationMapper;
import ru.practicum.location.model.Location;
import ru.practicum.location.storage.LocationStorage;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LocationService {
    private final LocationStorage storage;
    private final LocationMapper mapper;

    public Location get(LocationDto dto) {
        Optional<Location> location = storage.findByLatAndLon(dto.getLat(), dto.getLon());
        return location.orElseGet(() -> storage.save(mapper.toModel(dto)));
    }
}
