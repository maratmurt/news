package ru.skillbox.news.service;

import ru.skillbox.news.model.Comment;

import java.util.List;

public interface CommentService extends CrudService<Comment> {

    List<Comment> getAllByArticleId(Long articleId);

}
