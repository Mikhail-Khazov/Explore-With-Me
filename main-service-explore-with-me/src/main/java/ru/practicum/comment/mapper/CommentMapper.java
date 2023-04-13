package ru.practicum.comment.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.model.Comment;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class CommentMapper {
    private final UserMapper userMapper;
    private final EventMapper eventMapper;

    public Comment toModel(NewCommentDto dto, User author, Event event) {
        return Comment.builder()
                .author(author)
                .event(event)
                .text(dto.getText())
                .createdOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
    }

    public CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .author(userMapper.toShortDto(comment.getAuthor()))
                .event(eventMapper.toShortDto(comment.getEvent()))
                .text(comment.getText())
                .createdOn(comment.getCreatedOn())
                .build();
    }
}
