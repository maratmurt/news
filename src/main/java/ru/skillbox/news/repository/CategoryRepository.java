package ru.skillbox.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.news.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
