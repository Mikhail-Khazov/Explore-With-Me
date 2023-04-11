package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.storage.CommentStorage;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PrivateCommentService {
    private final CommentStorage storage;

    public CommentDto create(Long userId, Long eventId, NewCommentDto dto) {
        return null;
    }

    public CommentDto update(Long userId, Long commentId, NewCommentDto dto) {
        return null;
    }

    public void delete(Long userId, Long commentId) {
    }

    @Transactional(readOnly = true)
    public CommentDto get(Long userId, Long commentId) {
        return null;
    }

    public List<CommentDto> getAllForUser(Long userId) {
        return null;
    }
}
