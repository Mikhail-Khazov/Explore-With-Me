package ru.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.service.PrivateCommentService;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/comments")
@RequiredArgsConstructor
public class PrivateCommentController {
    private final PrivateCommentService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto create(@PathVariable Long userId, @RequestParam Long eventId, @Validated @RequestBody NewCommentDto dto) {
        return service.create(userId, eventId, dto);
    }

    @PatchMapping(path = "/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto update(@PathVariable Long userId, @PathVariable Long commentId, @Validated @RequestBody NewCommentDto dto) {
        return service.update(userId, commentId, dto);
    }

    @DeleteMapping(path = "/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId, @PathVariable Long commentId) {
        service.delete(userId, commentId);
    }

    @GetMapping(path = "/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto get(@PathVariable Long userId, @PathVariable Long commentId) {
        return service.get(userId, commentId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getAllForUser(@PathVariable Long userId) {
        return service.getAllForUser(userId);
    }
}
