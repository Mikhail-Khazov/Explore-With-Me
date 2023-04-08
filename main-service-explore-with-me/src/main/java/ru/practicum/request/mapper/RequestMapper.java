package ru.practicum.request.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.event.model.Event;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.Request;
import ru.practicum.user.model.User;

@Component
public class RequestMapper {
    public Request toModel(ParticipationRequestDto dto, Event event, User requester){
        return Request.builder()
                .id(dto.getId())
                .created(dto.getCreated())
                .event(event)
                .user(requester)
                .status(dto.getStatus())
                .build();
    }

    public ParticipationRequestDto toDto(Request model){
        return ParticipationRequestDto.builder()
                .id(model.getId())
                .created(model.getCreated())
                .event(model.getEvent().getId())
                .requester(model.getUser().getId())
                .status(model.getStatus())
                .build();
    }
}
