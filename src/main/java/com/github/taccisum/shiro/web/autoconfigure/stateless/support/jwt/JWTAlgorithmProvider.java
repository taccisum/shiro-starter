package com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt;

import com.auth0.jwt.algorithms.Algorithm;

import java.util.function.Supplier;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/6
 */
public interface JWTAlgorithmProvider extends Supplier<Algorithm> {
}
