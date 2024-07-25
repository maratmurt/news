package ru.skillbox.news.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.skillbox.news.model.Article;
import ru.skillbox.news.repository.ArticleRepository;
import ru.skillbox.news.service.ArticleService;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    @Override
    public Page<Article> getAll(int page, int size) {
        return articleRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Article getById(Long id) {
        return articleRepository.findById(id).orElseThrow();
    }

    @Override
    public Article create(Article article) {
        return articleRepository.save(article);
    }

    @Override
    public Article update(Article article) {
        Optional<Article> existingArticle = articleRepository.findById(article.getId());
        if (existingArticle.isEmpty()) {
            throw new NoSuchElementException("Статья не найдена!");
        }

        return articleRepository.save(article);
    }

    @Override
    public void deleteById(Long id) {
        articleRepository.deleteById(id);
    }
}
