package ru.skillbox.news.dto.comment;

public record CommentRequest(Long userId, Long articleId, String text) {
}
