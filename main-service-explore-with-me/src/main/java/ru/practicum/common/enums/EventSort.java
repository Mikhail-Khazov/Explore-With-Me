package ru.practicum.common.enums;

import org.springframework.data.domain.Sort;
import ru.practicum.common.exception.SortParamNotFoundException;

public enum EventSort {
    EVENT_DATE,
    VIEWS;

    private static Sort asc;

    public static Sort from(String sort) {
        if (null == sort) return Sort.by(Sort.DEFAULT_DIRECTION, "eventDate");
        for (EventSort value : EventSort.values()) {
            if (value.name().equalsIgnoreCase(sort)) {
                if (value == EventSort.EVENT_DATE) asc = Sort.by(Sort.DEFAULT_DIRECTION, "eventDate");
                if (value == EventSort.VIEWS) asc = Sort.by(Sort.DEFAULT_DIRECTION, "views");
                return asc;
            }
        }
        throw new SortParamNotFoundException("Incorrect sort param: " + sort);
    }
}
