package com.github.taccisum.shiro.web.autoconfigure.stateless.support;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/4
 */
public class StatelessToken implements AuthenticationToken {
    private String token;

    public StatelessToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
