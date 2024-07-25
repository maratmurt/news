package ru.skillbox.news.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;
import ru.skillbox.news.service.ArticleService;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Aspect
@Slf4j
public class AuthorIdChecker {

    private final ArticleService articleService;

    @Around("@annotation(CheckUserId)")
    public Object checkAuthorId(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest servletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        var pathVariables = (Map<String, String>) servletRequest.getAttribute(
                HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE
        );
        Long articleId = Long.parseLong(pathVariables.get("id"));
        Long authorId = articleService.getById(articleId).getAuthor().getId();
        String userIdString = servletRequest.getHeader("User-ID");
        if (userIdString == null || Long.parseLong(userIdString) != authorId) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Идентификатор пользователя не верный или отсутствует!");
        }

        return proceedingJoinPoint.proceed();
    }

}
