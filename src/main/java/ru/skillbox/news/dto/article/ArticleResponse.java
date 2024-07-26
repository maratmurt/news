package ru.skillbox.news.dto.article;

public record ArticleResponse(Long id, String title, String content, String author, String category, Long commentCount) {}
