package ru.skillbox.news.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.skillbox.news.model.Article;
import ru.skillbox.news.model.Comment;
import ru.skillbox.news.repository.ArticleRepository;
import ru.skillbox.news.repository.CommentRepository;
import ru.skillbox.news.service.CommentService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final ArticleRepository articleRepository;

    public List<Comment> getAllByArticleId(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow();
        return commentRepository.findAllByArticle(article);
    }

    @Override
    public Page<Comment> getAll(int page, int size) {
        return commentRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Comment getById(Long id) {
        return commentRepository.findById(id).orElseThrow();
    }

    @Override
    public Comment create(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Comment update(Comment comment) {
        Optional<Comment> existingArticle = commentRepository.findById(comment.getId());
        if (existingArticle.isEmpty()) {
            throw new NoSuchElementException("Статья не найдена!");
        }

        return commentRepository.save(comment);
    }

    @Override
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

    public Long countByArticleId(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow();
        return commentRepository.countByArticle(article);
    }
}
