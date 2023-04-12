package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.storage.CategoryStorage;
import ru.practicum.common.exception.CategoryNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PubicCategoryService {
    private final CategoryStorage storage;
    private final CategoryMapper mapper;

    public CategoryDto get(long catId) {
        Category category = storage.findById(catId).orElseThrow(CategoryNotFoundException::new);
        return mapper.toDto(category);
    }

    public List<CategoryDto> getAll(PageRequest pageRequest) {
        List<Category> categories = storage.findAll(pageRequest).toList();
        return categories.stream().map(mapper::toDto).collect(Collectors.toList());
    }
}
