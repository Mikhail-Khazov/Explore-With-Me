package ru.practicum.comment.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.comment.model.Comment;

public interface CommentStorage extends JpaRepository<Comment, Long> {
}
