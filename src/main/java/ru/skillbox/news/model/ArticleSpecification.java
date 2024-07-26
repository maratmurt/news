package ru.skillbox.news.model;

import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ArticleSpecification {

    public static Specification<Article> byCategories(List<Long> categoryIds) {
        return (root, query, criteriaBuilder) -> root.get("category").get("id").in(categoryIds);
    }

    public static Specification<Article> byAuthors(List<Long> authorIds) {
        return (root, query, criteriaBuilder) -> root.get("author").get("id").in(authorIds);
    }

}
