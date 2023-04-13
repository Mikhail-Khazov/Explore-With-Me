package ru.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.service.AdminCommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.common.Utils.DEFAULT_FROM_VALUE;
import static ru.practicum.common.Utils.DEFAULT_SIZE_VALUE;

@RestController
@RequestMapping(path = "/admin/comments")
@RequiredArgsConstructor
public class AdminCommentController {
    private final AdminCommentService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getByParams(@RequestParam(required = false) List<Long> authorId,
                                        @RequestParam(required = false) List<Long> eventId,
                                        @RequestParam(required = false) List<Long> commentId,
                                        @RequestParam(defaultValue = DEFAULT_FROM_VALUE) @PositiveOrZero int from,
                                        @RequestParam(defaultValue = DEFAULT_SIZE_VALUE) @Positive int size) {
        return service.getByParams(authorId, eventId, commentId, PageRequest.of(from / size, size));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestParam Long commentId){
        service.delete(commentId);
    }
}
