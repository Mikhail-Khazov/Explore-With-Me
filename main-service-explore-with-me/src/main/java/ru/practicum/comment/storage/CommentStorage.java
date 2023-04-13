package ru.practicum.comment.storage;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.comment.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentStorage extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {
    Optional<Comment> findByIdAndAuthorId(Long commentId, Long userId);

    List<Comment> findAllByAuthorId(Long authorId, PageRequest pageRequest);

    List<Comment> findAllByEventId(Long eventId, PageRequest pageRequest);
}
