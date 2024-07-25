package ru.skillbox.news.dto.comment;

import java.util.List;

public record CommentListResponse(List<CommentResponse> comments) {
}
