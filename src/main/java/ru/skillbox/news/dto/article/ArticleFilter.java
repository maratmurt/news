package ru.skillbox.news.dto.article;

import java.util.List;

public record ArticleFilter(List<Long> categoryIds, List<Long> authorIds) {
}
