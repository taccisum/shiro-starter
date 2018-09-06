package com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/6
 */
public class InvalidTokenException extends AuthenticationException {
    public InvalidTokenException(String token) {
        super(String.format("invalid token: %s", token));
    }
}
