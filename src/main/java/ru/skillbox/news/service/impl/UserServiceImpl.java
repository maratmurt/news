package ru.skillbox.news.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skillbox.news.model.RoleType;
import ru.skillbox.news.model.User;
import ru.skillbox.news.repository.UserRepository;
import ru.skillbox.news.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAll(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size)).toList();
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Пользователь с ID " + id + " не найден!"));
    }

    @Override
    public User create(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRoles(Set.of(RoleType.ROLE_USER));
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        Optional<User> existingUser = userRepository.findById(user.getId());
        if (existingUser.isEmpty()) {
            throw new NoSuchElementException("Пользователь с ID " + user.getId() + " не найден!");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRoles(Set.of(RoleType.ROLE_USER));

        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Пользователь с ID " + id + " не найден!"));
        userRepository.delete(user);
    }

    @Override
    public User getByName(String name) {
        return userRepository.findByName(name)
                .orElseThrow(() -> new NoSuchElementException("Пользователь с именем " + name + " не найден!"));
    }

}
