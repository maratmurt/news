package ru.skillbox.news.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import ru.skillbox.news.dto.article.ArticleRequest;
import ru.skillbox.news.dto.article.ArticleResponse;
import ru.skillbox.news.dto.article.ArticleWithCommentsResponse;
import ru.skillbox.news.model.Article;
import ru.skillbox.news.model.Category;
import ru.skillbox.news.model.User;
import ru.skillbox.news.repository.CategoryRepository;
import ru.skillbox.news.repository.UserRepository;

import java.util.NoSuchElementException;

@Mapper(componentModel = "spring", uses = {CommentMapper.class, UserRepository.class})
public abstract class ArticleMapper {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected CategoryRepository categoryRepository;

    @Mapping(target = "author", source = "author.name")
    @Mapping(target = "category", source = "category.name")
    @Mapping(target = "commentCount", source = "commentCount")
    public abstract ArticleResponse toResponse(Article article);

    @Mapping(target = "author", source = "author.name")
    @Mapping(target = "category", source = "category.name")
    public abstract ArticleWithCommentsResponse toWithCommentsResponse(Article article);

    @Mapping(target = "author", source = "authorId", qualifiedByName = "loadAuthorById")
    @Mapping(target = "category", source = "categoryId", qualifiedByName = "loadCategoryById")
    public abstract Article toEntity(ArticleRequest articleRequest);

    @Named("loadAuthorById")
    public User loadAuthorById(Long authorId) {
        return userRepository.findById(authorId).orElseThrow(() ->
                new NoSuchElementException("Автор не найден!"));
    }

    @Named("loadCategoryById")
    public Category loadCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() ->
                new NoSuchElementException("Категория не найдена!"));
    }

}
