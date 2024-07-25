package ru.skillbox.news.service;

import org.springframework.data.domain.Page;

public interface CrudService<T> {

    Page<T> getAll(int page, int size);

    T getById(Long id);

    T create(T item);

    T update (T item);

    void deleteById(Long id);

}
