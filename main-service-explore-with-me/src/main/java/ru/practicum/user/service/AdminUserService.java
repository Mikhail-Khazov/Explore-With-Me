package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.common.exception.UserConflictException;
import ru.practicum.common.exception.UserNotFoundException;
import ru.practicum.user.dto.NewUserDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class AdminUserService {
    private final UserMapper mapper;
    private final UserStorage storage;

    public UserDto create(NewUserDto newUserDto) {
        try {
            User user = mapper.toModel(newUserDto);
            return mapper.toDto(storage.save(user));
        } catch (Exception e) {
            throw new UserConflictException("Username or email already exist");
        }
    }

    @Transactional(readOnly = true)
    public List<UserDto> get(List<Long> ids, PageRequest pageRequest) {
        List<User> users;
        if (null == ids || ids.isEmpty()) {
            users = storage.findAll(pageRequest).toList();
        } else {
            users = storage.findAllByIds(ids, pageRequest);
        }
        return users.stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public void delete(Long userId) {
        if (storage.existsById(userId)) {
            storage.deleteById(userId);
        } else {
            throw new UserNotFoundException(String.format("User with Id %d does not exist", userId));
        }
    }

    public User getById(Long userId) {
        return storage.findById(userId).orElseThrow(() -> new UserNotFoundException(String.format("User with ID %d not found", userId)));
    }
}
