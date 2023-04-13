package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.storage.CommentStorage;
import ru.practicum.common.exception.NotFoundException;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminCommentService {
    private final CommentStorage storage;
    private final CommentMapper mapper;


    @Transactional(readOnly = true)
    public List<CommentDto> getByParams(List<Long> authorIds, List<Long> eventIds, List<Long> commentIds, PageRequest pageRequest) {
        Specification<Comment> spec = createSpecification(authorIds, eventIds, commentIds);
        List<Comment> comments = storage.findAll(spec, pageRequest).getContent();
        return comments.stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public void delete(Long commentId) {
        storage.findById(commentId).orElseThrow(() -> new NotFoundException("Comment with ID " + commentId + " not found"));
        storage.deleteById(commentId);
    }

    private Specification<Comment> createSpecification(List<Long> authorIds, List<Long> eventIds, List<Long> commentIds) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (null != authorIds && !authorIds.isEmpty()) {
                predicates.add(root.get("author").in(authorIds));
            }
            if (null != eventIds && !eventIds.isEmpty()) {
                predicates.add(root.get("event").in(eventIds));
            }
            if (null != commentIds && !commentIds.isEmpty()) {
                predicates.add(root.get("id").in(commentIds));
            }
            if (predicates.isEmpty()) {
                return criteriaBuilder.conjunction();
            } else {
                return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
            }
        };
    }
}
