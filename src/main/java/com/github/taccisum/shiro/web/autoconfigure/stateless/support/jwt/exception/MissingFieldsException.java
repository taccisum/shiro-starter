package com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt.exception;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/6
 */
public class MissingFieldsException extends BuildPayloadException {
    public MissingFieldsException(String message) {
        super(message);
    }
}
