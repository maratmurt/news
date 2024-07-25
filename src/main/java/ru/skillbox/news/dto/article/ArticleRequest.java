package ru.skillbox.news.dto.article;

public record ArticleRequest(Long authorId, Long categoryId, String title, String content) {
}
