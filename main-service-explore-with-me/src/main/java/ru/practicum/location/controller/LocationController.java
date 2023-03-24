package ru.practicum.location.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.location.service.LocationService;

@RestController
@RequiredArgsConstructor
public class LocationController {
    private final LocationService service;
}
