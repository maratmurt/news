package ru.skillbox.news.service;

import org.springframework.data.domain.Page;
import ru.skillbox.news.model.Article;

import java.util.List;

public interface ArticleService extends CrudService<Article> {

    Page<Article> getAllFiltered(int page, int size, List<Long> categoryIds, List<Long> authorIds);

}
