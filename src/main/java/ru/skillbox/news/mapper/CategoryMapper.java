package ru.skillbox.news.mapper;

import org.mapstruct.Mapper;
import ru.skillbox.news.dto.category.CategoryRequest;
import ru.skillbox.news.dto.category.CategoryResponse;
import ru.skillbox.news.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(CategoryRequest categoryRequest);

    CategoryResponse toResponse(Category category);

}
