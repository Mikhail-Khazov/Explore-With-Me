package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.storage.CommentStorage;
import ru.practicum.common.enums.EventState;
import ru.practicum.common.exception.RequestException;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.AdminEventService;
import ru.practicum.user.model.User;
import ru.practicum.user.service.AdminUserService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicCommentService {
    private final CommentStorage storage;
    private final CommentMapper mapper;
    private final AdminEventService eventService;
    private final AdminUserService userService;

    public List<CommentDto> getEventComments(Long eventId, Long userId, PageRequest pageRequest) {
        User user = null;
        if (null != userId) {
            user = userService.getById(userId);
        }
        Event event = eventService.getByIdForComment(eventId);
        if (null != user && !Objects.equals(user.getId(), event.getInitiator().getId()) && !event.getState().equals(EventState.PUBLISHED)) {
            throw new RequestException("Event not published");
        }
        return storage.findAllByEventId(eventId, pageRequest).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public List<Comment> getCommentsForEvent(List<Long> eventsId) {
        return storage.findAllByEventIdIn(eventsId);
    }
}
