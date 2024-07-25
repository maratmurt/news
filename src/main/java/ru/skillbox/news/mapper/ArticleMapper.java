package ru.skillbox.news.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.skillbox.news.dto.article.ArticleRequest;
import ru.skillbox.news.dto.article.ArticleResponse;
import ru.skillbox.news.dto.article.ArticleWithCommentsResponse;
import ru.skillbox.news.dto.comment.CommentResponse;
import ru.skillbox.news.model.Article;
import ru.skillbox.news.service.CommentService;
import ru.skillbox.news.service.UserService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ArticleMapper {

    private final UserService userService;

    private final CommentService commentService;

    private final CommentMapper commentMapper;

    public Article toEntity(ArticleRequest articleRequest) {
        Article article = new Article();

        article.setAuthor(userService.getById(articleRequest.authorId()));
        article.setTitle(articleRequest.title());
        article.setContent(articleRequest.content());

        return article;
    }

    public ArticleResponse toResponse(Article article) {
        return new ArticleResponse(
                article.getTitle(),
                article.getContent(),
                article.getAuthor().getName(),
                article.getCategory().getName(),
                commentService.countByArticleId(article.getId())
        );
    }

    public ArticleWithCommentsResponse toWithCommentsResponse(Article article) {
        List<CommentResponse> comments = commentService.getAllByArticleId(article.getId()).stream().map(commentMapper::toResponse).toList();
        return new ArticleWithCommentsResponse(
                article.getTitle(),
                article.getContent(),
                article.getAuthor().getName(),
                article.getCategory().getName(),
                comments
        );
    }
}
