package ru.skillbox.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.news.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
