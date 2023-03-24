package ru.practicum.location.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.location.storage.LocationStorage;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocationService {
    private final LocationStorage storage;
}

