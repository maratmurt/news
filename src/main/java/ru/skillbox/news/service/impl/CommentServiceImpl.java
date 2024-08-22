package ru.skillbox.news.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.skillbox.news.mapper.NullAwareMapper;
import ru.skillbox.news.model.Article;
import ru.skillbox.news.model.Comment;
import ru.skillbox.news.repository.ArticleRepository;
import ru.skillbox.news.repository.CommentRepository;
import ru.skillbox.news.service.CommentService;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final ArticleRepository articleRepository;

    private final NullAwareMapper nullAwareMapper;

    public List<Comment> getAllByArticleId(Long articleId) {
        return commentRepository.findAllByArticleId(articleId);
    }

    @Override
    public List<Comment> getAll(int page, int size) {
        return commentRepository.findAll(PageRequest.of(page, size)).toList();
    }

    @Override
    public Comment getById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Комментарий с ID " + id + " не найден!"));
    }

    @Override
    public Comment create(Comment comment) {
        Long articleId = comment.getArticle().getId();
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NoSuchElementException("Статья с ID " + articleId + " не найдена!"));
        articleRepository.save(article);
        return commentRepository.save(comment);
    }

    @Override
    public Comment update(Comment updatedComment) {
        Comment existingComment = commentRepository.findById(updatedComment.getId()).orElseThrow(()->
                new NoSuchElementException("Комментарий с ID " + updatedComment.getId() + " не найден!"));

        try {
            nullAwareMapper.copyProperties(existingComment, updatedComment);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return commentRepository.save(existingComment);
    }

    @Override
    public void deleteById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Комментарий с ID " + id + " не найден!"));
        commentRepository.delete(comment);
    }
}
