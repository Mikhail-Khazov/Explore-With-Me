package ru.practicum.location.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationStorage extends JpaRepository<Location, Long> {
}
