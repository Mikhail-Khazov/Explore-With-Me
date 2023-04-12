package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.common.exception.CompilationNotFoundException;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.storage.CompilationStorage;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PublicCompilationsService {
    private final CompilationStorage storage;
    private final CompilationMapper mapper;

    public CompilationDto get(Long compId) {
        Compilation compilation = storage.findById(compId).orElseThrow(CompilationNotFoundException::new);
        return mapper.toDto(compilation);
    }

    public List<CompilationDto> getAll(Boolean pinned, PageRequest pageRequest) {
        return null != pinned ?
                storage.findAllByPinnedIs(pinned, pageRequest).stream().map(mapper::toDto).collect(Collectors.toList()) :
                storage.findAll(pageRequest).stream().map(mapper::toDto).collect(Collectors.toList());
    }
}
