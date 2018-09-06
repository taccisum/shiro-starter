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

/**
 * todo:: unit test
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/5
 */
public class SimpleJWTRealm extends AuthorizingRealm {
    private JWTManager jwtManager;

    public SimpleJWTRealm(JWTManager jwtManager) {
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
            jwtManager.verify(token.getPrincipal().toString());
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
            payload = jwtManager.verifyAndParsePayload(token);
        } catch (JWTVerificationException e) {
            throw new InvalidTokenException(token);
        }
        Collection<String> roles = split(payload.get("roles").toString());
        Collection<String> permissions = split(payload.get("permissions").toString());
        SimpleAuthorizationInfo authzInfo = new SimpleAuthorizationInfo();
        authzInfo.addRoles(roles);
        authzInfo.addStringPermissions(permissions);
        return authzInfo;
    }

    static Collection<String> split(String roles) {
        return Arrays.asList(roles.split(","));
    }
}
