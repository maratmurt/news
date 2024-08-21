package ru.skillbox.news.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.skillbox.news.model.RoleType;
import ru.skillbox.news.model.User;
import ru.skillbox.news.service.ArticleService;
import ru.skillbox.news.service.CommentService;
import ru.skillbox.news.service.UserService;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Aspect
@Slf4j
public class SecurityAspect {

    private final UserService userService;

    private final ArticleService articleService;

    private final CommentService commentService;

    @Before("userGetByIdMethod()")
    public void secureUserGetById(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getByName(authentication.getName());
        List<RoleType> roles = currentUser.getRoles().stream().toList();
        if (roles.size() == 1 && roles.contains(RoleType.ROLE_USER)) {
            Long requestedUserId = Arrays.stream(joinPoint.getArgs())
                    .findFirst()
                    .map(obj -> (Long) obj)
                    .orElseThrow();

            if (!requestedUserId.equals(currentUser.getId())) {
                throw new SecurityException("Access denied");
            }
        }
    }

    @Before("updateMethod() && inArticles()")
    public void secureArticleUpdateMethod(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = userService.getByName(authentication.getName()).getId();

        Long articleId = Arrays.stream(joinPoint.getArgs())
                .filter(obj -> obj instanceof Long)
                .findFirst()
                .map(obj -> (Long) obj)
                .orElseThrow();
        Long authorId = articleService.getById(articleId).getAuthor().getId();

        if (!authorId.equals(currentUserId)) {
            throw new SecurityException("Access denied");
        }
    }

    @Before("updateMethod() && inComments()")
    public void secureCommentUpdateMethod(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = userService.getByName(authentication.getName()).getId();

        Long commentId = Arrays.stream(joinPoint.getArgs())
                .filter(obj -> obj instanceof Long)
                .findFirst()
                .map(obj -> (Long) obj)
                .orElseThrow();
        Long authorId = commentService.getById(commentId).getUser().getId();

        if (!authorId.equals(currentUserId)) {
            throw new SecurityException("Access denied");
        }
    }

    @Before("deleteMethod() && inArticles()")
    public void secureArticleDeleteMethod(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getByName(authentication.getName());
        List<RoleType> roles = currentUser.getRoles().stream().toList();

        if (roles.size() == 1 && roles.contains(RoleType.ROLE_USER)) {
            Long articleId = Arrays.stream(joinPoint.getArgs())
                    .filter(obj -> obj instanceof Long)
                    .findFirst()
                    .map(obj -> (Long) obj)
                    .orElseThrow();
            Long authorId = articleService.getById(articleId).getAuthor().getId();

            if (!authorId.equals(currentUser.getId())) {
                throw new SecurityException("Access denied");
            }
        }
    }

    @Before("deleteMethod() && inComments()")
    public void secureCommentDeleteMethod(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getByName(authentication.getName());
        List<RoleType> roles = currentUser.getRoles().stream().toList();

        if (roles.size() == 1 && roles.contains(RoleType.ROLE_USER)) {
            Long commentId = Arrays.stream(joinPoint.getArgs())
                    .filter(obj -> obj instanceof Long)
                    .findFirst()
                    .map(obj -> (Long) obj)
                    .orElseThrow();
            Long authorId = commentService.getById(commentId).getUser().getId();

            if (!authorId.equals(currentUser.getId())) {
                throw new SecurityException("Access denied");
            }
        }
    }

    @Pointcut("execution(* getById(..))")
    public void getByIdMethod() {}

    @Pointcut("execution(* update(..))")
    public void updateMethod() {}

    @Pointcut("execution(* delete(..))")
    public void deleteMethod() {}

    @Pointcut("execution(* ru.skillbox.news.controller.UserController.getById(..))")
    public void userGetByIdMethod() {}

    @Pointcut("within(ru.skillbox.news.controller.ArticleController)")
    public void inArticles() {}

    @Pointcut("within(ru.skillbox.news.controller.CommentController)")
    public void inComments() {}

}
