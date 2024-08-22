package ru.skillbox.news.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import ru.skillbox.news.dto.comment.CommentRequest;
import ru.skillbox.news.dto.comment.CommentResponse;
import ru.skillbox.news.model.Article;
import ru.skillbox.news.model.Comment;
import ru.skillbox.news.model.User;
import ru.skillbox.news.repository.ArticleRepository;
import ru.skillbox.news.repository.UserRepository;

@Mapper(componentModel = "spring", uses = {ArticleRepository.class, UserRepository.class})
public abstract class CommentMapper {

    @Autowired
    protected ArticleRepository articleRepository;

    @Autowired
    protected UserRepository userRepository;

    @Mapping(target = "article", source = "articleId", qualifiedByName = "loadArticleById")
    public abstract Comment toEntity(CommentRequest commentRequest);

    @Mapping(target = "userName", source = "user.name")
    public abstract CommentResponse toResponse(Comment comment);

    @Named("loadUserById")
    public User loadUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow();
    }

    @Named("loadArticleById")
    public Article loadArticleById(Long articleId) {
        return articleRepository.findById(articleId).orElseThrow();
    }

}
