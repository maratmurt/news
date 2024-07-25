package ru.skillbox.news.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.skillbox.news.dto.comment.CommentRequest;
import ru.skillbox.news.dto.comment.CommentResponse;
import ru.skillbox.news.model.Comment;
import ru.skillbox.news.service.ArticleService;
import ru.skillbox.news.service.UserService;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final ArticleService articleService;

    private final UserService userService;

    public Comment toEntity(CommentRequest commentRequest) {
        Comment comment = new Comment();

        comment.setArticle(articleService.getById(commentRequest.articleId()));
        comment.setUser(userService.getById(commentRequest.userId()));
        comment.setText(commentRequest.text());

        return comment;
    }

    public CommentResponse toResponse(Comment comment) {
        return new CommentResponse(comment.getUser().getName(), comment.getText());
    }

}
