package ru.practicum.event.storage;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.event.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventStorage extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    Optional<Event> findById(Long id);

    List<Event> findByInitiatorId(Long userId, PageRequest pageRequest);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);

    Boolean existsByCategoryId(Long catId);
}
