package com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.StatelessToken;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt.exception.InvalidTokenException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * todo:: unit test
 *
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/5
 */
public class SimpleJWTRealm extends AuthorizingRealm {
    private String issuer;
    private JWTManager jwtManager;

    public SimpleJWTRealm(String issuer, JWTManager jwtManager) {
        Objects.requireNonNull(issuer, "issuer can not be null");
        Objects.requireNonNull(jwtManager, "jwt manager can not be null");
        this.issuer = issuer;
        this.jwtManager = jwtManager;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof StatelessToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        StatelessToken token = (StatelessToken) authenticationToken;
        try {
            jwtManager.verify(issuer, token.getPrincipal().toString());
        } catch (JWTVerificationException e) {
            throw new InvalidTokenException(token.getToken());
        }
        return new SimpleAccount(token.getPrincipal().toString(), null, this.getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String token = principalCollection.fromRealm(this.getName()).iterator().next().toString();
        Payload payload;
        try {
            payload = jwtManager.verifyAndParsePayload(issuer, token);
        } catch (JWTVerificationException e) {
            throw new InvalidTokenException(token);
        }
        SimpleAuthorizationInfo authzInfo = new SimpleAuthorizationInfo();
        if (jwtManager.getPayloadTemplate(issuer).hasField("roles", String.class)) {
            authzInfo.addRoles(split(payload.get("roles").toString()));
        }
        if (jwtManager.getPayloadTemplate(issuer).hasField("permissions", String.class)) {
            authzInfo.addStringPermissions(split(payload.get("permissions").toString()));
        }
        return authzInfo;
    }

    static Collection<String> split(String roles) {
        return Arrays.asList(roles.split(","));
    }
}
