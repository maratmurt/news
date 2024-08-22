package ru.skillbox.news.dto.article;

public record ArticleRequest(Long categoryId, String title, String content) {
}
