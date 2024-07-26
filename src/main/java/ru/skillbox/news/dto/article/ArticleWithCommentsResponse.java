package ru.skillbox.news.dto.article;

import ru.skillbox.news.dto.comment.CommentResponse;

import java.util.List;

public record ArticleWithCommentsResponse(Long id, String title, String content, String author, String category, List<CommentResponse> comments) {
}
