package com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt.exception;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/6
 */
public class ParsePayloadException extends RuntimeException {
    public ParsePayloadException(String message) {
        super(message);
    }
}
