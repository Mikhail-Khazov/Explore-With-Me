package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.storage.CategoryStorage;
import ru.practicum.common.exception.CategoryNotFoundException;
import ru.practicum.common.exception.EntityAlreadyExistException;
import ru.practicum.common.exception.RequestException;
import ru.practicum.event.storage.EventStorage;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminCategoryService {
    private final CategoryStorage storage;
    private final CategoryMapper mapper;
    private final EventStorage eventStorage;

    public CategoryDto create(NewCategoryDto newCategoryDto) {
        if (storage.existsByName(newCategoryDto.getName()))
            throw new EntityAlreadyExistException("Category with that name already exist");
        Category category = mapper.toModel(newCategoryDto);
        return mapper.toDto(storage.save(category));
    }

    public CategoryDto update(long catId, NewCategoryDto categoryDto) {
        Category category = storage.findById(catId).orElseThrow(CategoryNotFoundException::new);
        if (storage.existsByName(categoryDto.getName()))
            throw new EntityAlreadyExistException("Category with that name already exist");
        category.setName(categoryDto.getName());
        return mapper.toDto(category);
    }

    public void delete(long catId) {
        storage.findById(catId).orElseThrow(CategoryNotFoundException::new);
        if (eventStorage.existsByCategoryId(catId)) throw new RequestException("Category not empty");
        storage.deleteById(catId);
    }

    @Transactional(readOnly = true)
    public Category getById(Long catId) {
        return storage.findById(catId).orElseThrow(CategoryNotFoundException::new);
    }
}
