package com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt;

/**
 * todo:: unit test
 *
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/5
 */
public class SimpleJWTRealm extends AbstractJWTRealm {
    public SimpleJWTRealm(String issuer, JWTManager jwtManager) {
        super(issuer, jwtManager);
    }
}
