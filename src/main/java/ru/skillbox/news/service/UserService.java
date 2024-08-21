package ru.skillbox.news.service;

import ru.skillbox.news.model.User;

public interface UserService extends CrudService<User> {

    User getByName(String name);

}
