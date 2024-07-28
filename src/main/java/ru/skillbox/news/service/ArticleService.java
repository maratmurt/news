package ru.skillbox.news.service;

import ru.skillbox.news.model.Article;

import java.util.List;

public interface ArticleService extends CrudService<Article> {

    List<Article> getAllFiltered(int page, int size, List<Long> categoryIds, List<Long> authorIds);

}
