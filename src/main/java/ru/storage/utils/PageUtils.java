package ru.storage.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PageUtils<T> {

    //T object;

    public Page<T> createPage(List<T> listObjects, int pageNumber, int pageSize, long totalElements) {
        // Данные (контент текущей страницы)
        List<T> content = listObjects;

        // Настройки пагинации (номер страницы, размер страницы)
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        // Создание объекта Page
        Page<T> page = new PageImpl<>(content, pageable, totalElements);

        return page;

    }

    public Page<T> createPage(List<T> listObjects, int pageNumber, int pageSize) {

        long totalElements = 50; // 3. Общее количество элементов во всей выборке (не только в этом списке)
        Page<T> page = createPage(listObjects, pageNumber, pageSize, totalElements);

        return page;

    }

    public Page<T> createPage(List<T> listObjects, int pageNumber) {

        long totalElements = 50; // 3. Общее количество элементов во всей выборке (не только в этом списке)
        int pageSize = 5; // 2. Размер одной "страницы"
        Page<T> page = createPage(listObjects, pageNumber, pageSize, totalElements);
        return page;
    }

    public Page<T> createPage(List<T> listObjects) {

        long totalElements = 50; // 3. Общее количество элементов во всей выборке (не только в этом списке)
        int pageSize = 5; // 2. Размер одной "страницы"
        int pageNumber = 0; // 1. Текущая страница
        Page<T> page = createPage(listObjects, pageNumber, pageSize, totalElements);
        return page;
    }
}
