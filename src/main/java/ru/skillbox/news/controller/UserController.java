package ru.skillbox.news.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.news.dto.user.UserListResponse;
import ru.skillbox.news.dto.user.UserRequest;
import ru.skillbox.news.dto.user.UserResponse;
import ru.skillbox.news.mapper.UserMapper;
import ru.skillbox.news.model.User;
import ru.skillbox.news.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserListResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        List<UserResponse> users = userService.getAll(page, size).stream()
                .map(userMapper::toResponse).toList();
        return ResponseEntity.ok(new UserListResponse(users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userMapper.toResponse(userService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody UserRequest userRequest) {
        User user = userService.create(userMapper.toEntity(userRequest));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userMapper.toResponse(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @RequestBody UserRequest request) {
        User user = userMapper.toEntity(request);
        user.setId(id);
        return ResponseEntity.ok(userMapper.toResponse(userService.update(user)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
