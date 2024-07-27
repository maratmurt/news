package ru.skillbox.news.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.news.dto.comment.CommentListResponse;
import ru.skillbox.news.dto.comment.CommentRequest;
import ru.skillbox.news.dto.comment.CommentResponse;
import ru.skillbox.news.mapper.CommentMapper;
import ru.skillbox.news.model.Comment;
import ru.skillbox.news.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    private final CommentMapper commentMapper;

    @GetMapping
    public ResponseEntity<CommentListResponse> getAll(@RequestParam Long articleId) {
        List<CommentResponse> comments = commentService.getAllByArticleId(articleId).stream()
                .map(commentMapper::toResponse).toList();
        return ResponseEntity.ok(new CommentListResponse(comments));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(commentMapper.toResponse(commentService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<CommentResponse> create(@RequestBody CommentRequest commentRequest) {
        Comment comment = commentService.create(commentMapper.toEntity(commentRequest));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentMapper.toResponse(comment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> update(@PathVariable Long id, @RequestBody CommentRequest request) {
        Comment comment = commentMapper.toEntity(request);
        comment.setId(id);
        return ResponseEntity.ok(commentMapper.toResponse(commentService.update(comment)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        commentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
