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
        User currentUser = getCurrentUser();
        List<RoleType> roles = currentUser.getRoles().stream().toList();
        if (roles.size() == 1 && roles.contains(RoleType.ROLE_USER)) {
            Long requestedUserId = getIdFromArgs(joinPoint.getArgs());

            if (!requestedUserId.equals(currentUser.getId())) {
                throw new SecurityException("Доступ к данным других пользователей ограничен");
            }
        }
    }

    @Before("updateMethod() && inArticles()")
    public void secureArticleUpdateMethod(JoinPoint joinPoint) {
        User currentUser = getCurrentUser();
        Long articleId = getIdFromArgs(joinPoint.getArgs());
        Long authorId = articleService.getById(articleId).getAuthor().getId();

        if (!authorId.equals(currentUser.getId())) {
            throw new SecurityException("Допускается редактирование только своего контента");
        }
    }

    @Before("updateMethod() && inComments()")
    public void secureCommentUpdateMethod(JoinPoint joinPoint) {
        User currentUser = getCurrentUser();
        Long commentId = getIdFromArgs(joinPoint.getArgs());
        Long authorId = commentService.getById(commentId).getUser().getId();

        if (!authorId.equals(currentUser.getId())) {
            throw new SecurityException("Допускается редактирование только своих комментариев");
        }
    }

    @Before("deleteMethod() && inArticles()")
    public void secureArticleDeleteMethod(JoinPoint joinPoint) {
        User currentUser = getCurrentUser();
        List<RoleType> roles = currentUser.getRoles().stream().toList();
        if (roles.size() == 1 && roles.contains(RoleType.ROLE_USER)) {
            Long articleId = getIdFromArgs(joinPoint.getArgs());
            Long authorId = articleService.getById(articleId).getAuthor().getId();

            if (!authorId.equals(currentUser.getId())) {
                throw new SecurityException("Допускается удаление только своего контента");
            }
        }
    }

    @Before("deleteMethod() && inComments()")
    public void secureCommentDeleteMethod(JoinPoint joinPoint) {
        User currentUser = getCurrentUser();
        List<RoleType> roles = currentUser.getRoles().stream().toList();
        if (roles.size() == 1 && roles.contains(RoleType.ROLE_USER)) {
            Long commentId = getIdFromArgs(joinPoint.getArgs());
            Long authorId = commentService.getById(commentId).getUser().getId();

            if (!authorId.equals(currentUser.getId())) {
                throw new SecurityException("Допускается удаление только своих комментариев");
            }
        }
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.getByName(authentication.getName());
    }

    private Long getIdFromArgs(Object[] args) {
        return Arrays.stream(args)
                .filter(obj -> obj instanceof Long)
                .findFirst()
                .map(obj -> (Long) obj)
                .orElseThrow();
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
