package com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt.exception;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/12
 */
public class NotExistPayloadTemplateException extends RuntimeException {
    public NotExistPayloadTemplateException(String message) {
        super(message);
    }
}
