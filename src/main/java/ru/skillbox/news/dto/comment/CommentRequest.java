package ru.skillbox.news.dto.comment;

public record CommentRequest(Long articleId, String text) {
}
