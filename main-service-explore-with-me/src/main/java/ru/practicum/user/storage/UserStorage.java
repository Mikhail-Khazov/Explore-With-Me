package ru.practicum.user.storage;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    @Query("select u from User u where u.id in ?1")
    List<User> findAllByIds(List<Long> ids, PageRequest pageRequest);

    Optional<User> findByName(String name);
}
