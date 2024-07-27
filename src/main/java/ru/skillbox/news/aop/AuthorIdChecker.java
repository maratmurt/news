package ru.skillbox.news.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;
import ru.skillbox.news.repository.ArticleRepository;
import ru.skillbox.news.repository.CommentRepository;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Aspect
@Slf4j
public class AuthorIdChecker {

    private final ArticleRepository articleRepository;

    private final CommentRepository commentRepository;

    @Around("updateArticleMethod() || deleteArticleMethod()")
    public Object checkArticleAuthorId(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest servletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        var pathVariables = (Map<String, String>) servletRequest.getAttribute(
                HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE
        );
        Long articleId = Long.parseLong(pathVariables.get("id"));
        Long authorId = articleRepository.findById(articleId).orElseThrow().getAuthor().getId();
        String userIdString = servletRequest.getHeader("User-ID");
        if (userIdString == null || Long.parseLong(userIdString) != authorId) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Идентификатор пользователя не верный или отсутствует!");
        }

        return proceedingJoinPoint.proceed();
    }

    @Around("updateCommentMethod() || deleteCommentMethod()")
    public Object checkCommentAuthorId(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest servletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        var pathVariables = (Map<String, String>) servletRequest.getAttribute(
                HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE
        );
        Long commentId = Long.parseLong(pathVariables.get("id"));
        Long authorId = commentRepository.findById(commentId).orElseThrow().getUser().getId();
        String userIdString = servletRequest.getHeader("User-ID");
        if (userIdString == null || Long.parseLong(userIdString) != authorId) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Идентификатор пользователя не верный или отсутствует!");
        }

        return proceedingJoinPoint.proceed();
    }

    static class Pointcuts {

        @Pointcut("execution(* ru.skillbox.news.controller.ArticleController.update(..))")
        public void updateArticleMethod() {}

        @Pointcut("execution(* ru.skillbox.news.controller.ArticleController.delete(..))")
        public void deleteArticleMethod() {}

        @Pointcut("execution(* ru.skillbox.news.controller.CommentController.update(..))")
        public void updateCommentMethod() {}

        @Pointcut("execution(* ru.skillbox.news.controller.CommentController.delete(..))")
        public void deleteCommentMethod() {}

    }

}
