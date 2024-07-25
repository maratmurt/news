package ru.skillbox.news.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.news.dto.category.CategoryListResponse;
import ru.skillbox.news.dto.category.CategoryRequest;
import ru.skillbox.news.dto.category.CategoryResponse;
import ru.skillbox.news.mapper.CategoryMapper;
import ru.skillbox.news.model.Category;
import ru.skillbox.news.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<CategoryListResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        List<CategoryResponse> categories = categoryService.getAll(page, size).stream()
                .map(categoryMapper::toResponse).toList();
        return ResponseEntity.ok(new CategoryListResponse(categories));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryMapper.toResponse(categoryService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@RequestBody CategoryRequest categoryRequest) {
        Category category = categoryService.create(categoryMapper.toEntity(categoryRequest));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryMapper.toResponse(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(@PathVariable Long id, @RequestBody CategoryRequest categoryRequest) {
        Category category = categoryMapper.toEntity(categoryRequest);
        category.setId(id);
        return ResponseEntity.ok(categoryMapper.toResponse(categoryService.update(category)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
