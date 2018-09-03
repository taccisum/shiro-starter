package com.github.taccisum.shiro.web.autoconfigure;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/3
 */
@Configuration
@ConditionalOnProperty(name = "shiro.web.rest.enabled", havingValue = "true")
@RestControllerAdvice
public class RestModeAutoConfiguration {
    @ExceptionHandler(UnauthorizedException.class)
    public Object handleAuthorizationException(AuthorizationException e) {
        return e.getMessage();
    }
}
