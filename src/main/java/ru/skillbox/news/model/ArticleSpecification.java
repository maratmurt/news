package ru.skillbox.news.model;

import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ArticleSpecification {

    public static Specification<Article> byCategories(List<Category> categories) {
        return (root, query, criteriaBuilder) -> root.get("category").in(categories);
    }

    public static Specification<Article> byAuthors(List<User> authors) {
        return (root, query, criteriaBuilder) -> root.get("author").in(authors);
    }

}
