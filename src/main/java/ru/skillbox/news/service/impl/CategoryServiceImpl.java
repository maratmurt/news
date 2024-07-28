package ru.skillbox.news.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.skillbox.news.model.Category;
import ru.skillbox.news.repository.CategoryRepository;
import ru.skillbox.news.service.CategoryService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAll(int page, int size) {
        return categoryRepository.findAll(PageRequest.of(page, size)).toList();
    }

    @Override
    public Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Категория с ID " + id + " не найдена!"));
    }

    @Override
    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Category category) {
        Optional<Category> existingUser = categoryRepository.findById(category.getId());
        if (existingUser.isEmpty()) {
            throw new NoSuchElementException("Категория с ID " + category.getId() + " не найдена!");
        }

        return categoryRepository.save(category);
    }

    @Override
    public void deleteById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Категория с ID " + id + " не найдена!"));
        categoryRepository.delete(category);
    }
}
