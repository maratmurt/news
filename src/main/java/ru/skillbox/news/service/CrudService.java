package ru.skillbox.news.service;

import java.util.List;

public interface CrudService<T> {

    List<T> getAll(int page, int size);

    T getById(Long id);

    T create(T item);

    T update (T item);

    void deleteById(Long id);

}
