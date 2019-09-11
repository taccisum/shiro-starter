package com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt;

import com.github.taccisum.shiro.web.autoconfigure.stateless.support.StatelessCredentialsMatcher;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.StatelessToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;

/**
 * @author xiangtch
 * @date 2019/9/11 21:28
 * <p> Email: xiangtiancheng@deepexi.com </p>
 */
public class OnlyParseJWTRealm extends AbstractJWTRealm {

    public OnlyParseJWTRealm(String issuer, JWTManager jwtManager) {
        super(issuer, jwtManager);
        setCredentialsMatcher(new StatelessCredentialsMatcher());
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        if (!(authenticationToken instanceof StatelessToken)){
            throw new RuntimeException("authenticationToken is not a instance of StatelessToken");
        }
        StatelessToken token = (StatelessToken)authenticationToken;
        String jwt = token.getPrincipal().toString();
        Payload payload = jwtManager.parsePayload(jwt);
        return new SimpleAccount(new OnlyParseJWTPrincipal(jwt, payload), null, this.getName());
    }
}
