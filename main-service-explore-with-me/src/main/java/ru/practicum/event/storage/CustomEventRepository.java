package ru.practicum.event.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.practicum.category.service.AdminCategoryService;
import ru.practicum.common.exception.LocationNotFoundException;
import ru.practicum.event.dto.UpdateEventDto;
import ru.practicum.event.model.EnterParams;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.Location;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomEventRepository {
    private final LocationStorage locationStorage;
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
            if (params.getOnlyAvailable() != null) {
                predicates.add(cb.lessThan(cb.size(root.get("confirmedRequests")), root.get("participantLimit")));
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
        if (null != dto.getAnnotation()) event.setAnnotation(dto.getAnnotation());
        if (null != dto.getCategory()) event.setCategory(categoryService.getById(dto.getCategory()));
        if (null != dto.getDescription()) event.setDescription(dto.getDescription());
        if (null != dto.getEventDate()) event.setEventDate(dto.getEventDate());
        if (null != dto.getLocation()) {
            Location location = locationStorage.findById(event.getLocation().getId()).orElseThrow(LocationNotFoundException::new);
            if (null != dto.getLocation().getLat() && null != dto.getLocation().getLon()) {
                location.setLat(dto.getLocation().getLat());
                location.setLon(dto.getLocation().getLon());
            }
        }
        if (null != dto.getPaid()) event.setPaid(dto.getPaid());
        if (null != dto.getParticipantLimit()) event.setParticipantLimit(dto.getParticipantLimit());
        if (null != dto.getRequestModeration()) event.setRequestModeration(dto.getRequestModeration());
        if (null != dto.getTitle()) event.setTitle(dto.getTitle());
    }
}
