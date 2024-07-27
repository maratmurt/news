package ru.skillbox.news.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.news.dto.article.*;
import ru.skillbox.news.mapper.ArticleMapper;
import ru.skillbox.news.model.Article;
import ru.skillbox.news.service.ArticleService;

import java.util.List;

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    private final ArticleMapper articleMapper;

    @GetMapping
    public ResponseEntity<ArticleListResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size,
                                                      @RequestBody(required = false) ArticleFilter filter) {
        List<Long> categoryIds = null, authorIds = null;
        if (filter != null) {
            categoryIds = filter.categoryIds();
            authorIds = filter.authorIds();
        }
        List<ArticleResponse> articles = articleService
                .getAllFiltered(page, size, categoryIds, authorIds)
                .map(articleMapper::toResponse).toList();
        return ResponseEntity.ok(new ArticleListResponse(articles));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleWithCommentsResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(articleMapper.toWithCommentsResponse(articleService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ArticleWithCommentsResponse> create(@RequestBody ArticleRequest articleRequest) {
        Article article = articleService.create(articleMapper.toEntity(articleRequest));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleMapper.toWithCommentsResponse(article));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleResponse> update(@PathVariable Long id, @RequestBody ArticleRequest request) {
        Article article = articleMapper.toEntity(request);
        article.setId(id);
        return ResponseEntity.ok(articleMapper.toResponse(articleService.update(article)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        articleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
