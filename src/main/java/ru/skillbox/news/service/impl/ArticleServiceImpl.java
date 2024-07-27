package ru.skillbox.news.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.skillbox.news.model.Article;
import ru.skillbox.news.model.ArticleSpecification;
import ru.skillbox.news.repository.ArticleRepository;
import ru.skillbox.news.service.ArticleService;

import java.util.List;
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
        return articleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Статья с ID " + id + " не найдена!"));
    }

    @Override
    public Article create(Article article) {
        return articleRepository.save(article);
    }

    @Override
    public Article update(Article article) {
        Optional<Article> existingArticle = articleRepository.findById(article.getId());
        if (existingArticle.isEmpty()) {
            throw new NoSuchElementException("Статья с ID " + article.getId() + " не найдена!");
        }

        return articleRepository.save(article);
    }

    @Override
    public void deleteById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Статья с ID " + id + " не найдена!"));
        articleRepository.delete(article);
    }

    @Override
    public Page<Article> getAllFiltered(int page, int size, List<Long> categoryIds, List<Long> authorIds) {
        Specification<Article> specification = Specification.where(null);

        if (categoryIds != null && !categoryIds.isEmpty()) {
            specification = specification.and(ArticleSpecification.byCategories(categoryIds));
        }

        if (authorIds != null && !authorIds.isEmpty()) {
            specification = specification.and(ArticleSpecification.byAuthors(authorIds));
        }

        return articleRepository.findAll(specification, PageRequest.of(page, size));
    }
}
