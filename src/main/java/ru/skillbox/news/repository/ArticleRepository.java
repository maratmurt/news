package ru.skillbox.news.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.skillbox.news.model.Article;

import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>, JpaSpecificationExecutor<Article> {
    @Override
    @NonNull
    @EntityGraph(attributePaths = {"comments"})
    Optional<Article> findById(@NonNull Long id);
}
