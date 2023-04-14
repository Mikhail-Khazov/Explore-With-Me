package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.storage.CommentStorage;
import ru.practicum.common.enums.EventState;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.common.exception.RequestException;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.AdminEventService;
import ru.practicum.user.model.User;
import ru.practicum.user.service.AdminUserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PrivateCommentService {
    private final CommentStorage storage;
    private final CommentMapper mapper;
    private final AdminUserService userService;
    private final AdminEventService eventService;

    public CommentDto create(Long authorId, Long eventId, NewCommentDto dto) {
        User author = userService.getById(authorId);
        Event event = eventService.getByIdForComment(eventId);
        if (!Objects.equals(author.getId(), event.getInitiator().getId()) && !event.getState().equals(EventState.PUBLISHED)) {
            throw new RequestException("Event not published");
        }
        Comment comment = mapper.toModel(dto, author, event);
        return mapper.toDto(storage.save(comment));
    }

    public CommentDto update(Long authorId, Long commentId, NewCommentDto dto) {
        Comment comment = getUserComment(authorId, commentId);
        if (comment.getCreatedOn().plusHours(1).isBefore(LocalDateTime.now())) {
            throw new RequestException("Editing is forbidden");
        }
        comment.setText(dto.getText());
        comment.setUpdatedOn(LocalDateTime.now());
        return mapper.toDto(comment);
    }

    public void delete(Long authorId, Long commentId) {
        getUserComment(authorId, commentId);
        storage.deleteById(commentId);
    }

    @Transactional(readOnly = true)
    public List<CommentDto> getAllForUser(Long authorId, PageRequest pageRequest) {
        userService.getById(authorId);
        List<Comment> comments = storage.findAllByAuthorId(authorId, pageRequest);
        return comments.stream().map(mapper::toDto).collect(Collectors.toList());
    }

    private Comment getUserComment(Long authorId, Long commentId) {
        return storage.findByIdAndAuthorId(commentId, authorId)
                .orElseThrow(() -> new NotFoundException("Comment not found or doesn't belong to user"));
    }
}
