package com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt;

import com.auth0.jwt.algorithms.Algorithm;

import java.util.UUID;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/6
 */
public class DefaultJWTAlgorithmProvider implements JWTAlgorithmProvider {
    private static final String SECRET = UUID.randomUUID().toString();
    private static Algorithm algorithm;

    @Override
    public Algorithm get() {
        if (algorithm == null) {
            algorithm = Algorithm.HMAC256(SECRET);
        }
        return algorithm;
    }
}
