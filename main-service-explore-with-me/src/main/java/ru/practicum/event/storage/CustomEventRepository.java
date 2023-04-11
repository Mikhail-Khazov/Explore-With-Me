package ru.practicum.event.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.practicum.category.service.AdminCategoryService;
import ru.practicum.common.enums.RequestStatus;
import ru.practicum.event.dto.UpdateEventDto;
import ru.practicum.event.model.EnterParams;
import ru.practicum.event.model.Event;
import ru.practicum.location.service.LocationService;
import ru.practicum.request.model.Request;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomEventRepository {
    private final LocationService locationService;
    private final AdminCategoryService categoryService;

    public Specification<Event> createSpecification(EnterParams params) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (params.getUsers() != null && !params.getUsers().isEmpty()) {
                predicates.add(root.get("initiator").in(params.getUsers()));
            }
            if (params.getStates() != null && !params.getStates().isEmpty()) {
                predicates.add(root.get("state").in(params.getStates()));
            }
            if (params.getCategories() != null && !params.getCategories().isEmpty()) {
                predicates.add(root.get("category").in(params.getCategories()));
            }
            if (params.getRangeStart() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("eventDate"), params.getRangeStart()));
            }
            if (params.getRangeEnd() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("eventDate"), params.getRangeEnd()));
            }

            if (params.getOnlyAvailable() != null && params.getOnlyAvailable()) {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<Request> subRoot = subquery.from(Request.class);
                subquery.select(cb.count(subRoot));
                subquery.where(cb.equal(subRoot.get("event"), root), cb.equal(subRoot.get("status"), RequestStatus.CONFIRMED));
                predicates.add(cb.greaterThan(root.get("participantLimit"), subquery));
            }

            if (params.getPaid() != null) {
                predicates.add(root.get("paid").in(params.getPaid()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Specification<Event> createSpecificationAnnotation(EnterParams params) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("annotation")), "%" + params.getText().toLowerCase() + "%");
    }

    public Specification<Event> createSpecificationDescription(EnterParams params) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("description")), "%" + params.getText().toLowerCase() + "%");
    }

    public void updateEventFields(Event event, UpdateEventDto dto) {
        if (null != dto.getAnnotation() && !dto.getAnnotation().isBlank()) event.setAnnotation(dto.getAnnotation());
        if (null != dto.getCategory()) event.setCategory(categoryService.getById(dto.getCategory()));
        if (null != dto.getDescription() && !dto.getDescription().isBlank()) event.setDescription(dto.getDescription());
        if (null != dto.getEventDate()) event.setEventDate(dto.getEventDate());
        if (null != dto.getLocation()) locationService.get(dto.getLocation());
        if (null != dto.getPaid()) event.setPaid(dto.getPaid());
        if (null != dto.getParticipantLimit()) event.setParticipantLimit(dto.getParticipantLimit());
        if (null != dto.getRequestModeration()) event.setRequestModeration(dto.getRequestModeration());
        if (null != dto.getTitle() && !dto.getTitle().isBlank()) event.setTitle(dto.getTitle());
    }
}
