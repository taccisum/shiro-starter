package com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt;

/**
 * Created By @author Zhouyuhang on 2019/10/15
 * <p></p>
 **/
public class MultiJWTRealm extends SimpleJWTRealm {
    public MultiJWTRealm(String issuer, JWTCustomManager jwtManager) {
        super(issuer, jwtManager);
    }
}
