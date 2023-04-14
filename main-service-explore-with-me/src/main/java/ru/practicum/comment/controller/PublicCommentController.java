package ru.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.service.PublicCommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.common.Utils.DEFAULT_FROM_VALUE;
import static ru.practicum.common.Utils.DEFAULT_SIZE_VALUE;

@RestController
@RequestMapping(path = "/event/{eventId}/comments")
@RequiredArgsConstructor
@Validated
public class PublicCommentController {
    private final PublicCommentService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getEventComments(@PathVariable Long eventId,
                                             @RequestHeader(value = "User-Id", required = false) Long userId,
                                             @RequestParam(defaultValue = DEFAULT_FROM_VALUE) @PositiveOrZero int from,
                                             @RequestParam(defaultValue = DEFAULT_SIZE_VALUE) @Positive int size) {
        return service.getEventComments(eventId, userId, PageRequest.of(from / size, size));
    }
}
