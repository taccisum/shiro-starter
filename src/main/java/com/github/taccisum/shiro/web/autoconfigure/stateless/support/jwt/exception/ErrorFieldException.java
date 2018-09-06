package com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt.exception;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/6
 */
public class ErrorFieldException extends BuildPayloadException {
    public ErrorFieldException(String message) {
        super(message);
    }
}
