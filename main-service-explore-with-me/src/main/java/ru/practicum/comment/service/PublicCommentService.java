package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.storage.CommentStorage;
import ru.practicum.common.exception.EventNotFoundException;
import ru.practicum.event.service.AdminEventService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicCommentService {
    private final CommentStorage storage;
    private final CommentMapper mapper;
    private final AdminEventService eventService;
    public List<CommentDto> getEventComments(Long eventId, PageRequest pageRequest) {
        eventService.getByIdForComment(eventId);
        return storage.findAllByEventId(eventId, pageRequest).stream().map(mapper::toDto).collect(Collectors.toList());
    }
}
