package ru.skillbox.news.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.news.dto.article.ArticleRequest;
import ru.skillbox.news.dto.article.ArticleResponse;
import ru.skillbox.news.dto.article.ArticleWithCommentsResponse;
import ru.skillbox.news.model.Article;

@Mapper(componentModel = "spring", uses = {CommentMapper.class})
public interface ArticleMapper {

    @Mapping(target = "author", source = "author.name")
    @Mapping(target = "category", source = "category.name")
    ArticleResponse toResponse(Article article);

    @Mapping(target = "author", source = "author.name")
    @Mapping(target = "category", source = "category.name")
    ArticleWithCommentsResponse toWithCommentsResponse(Article article);

    Article toEntity(ArticleRequest articleRequest);

}
