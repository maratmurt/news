package ru.skillbox.news.dto.article;

public record ArticleResponse(String title, String content, String author, String category, Long commentsCount) {}
