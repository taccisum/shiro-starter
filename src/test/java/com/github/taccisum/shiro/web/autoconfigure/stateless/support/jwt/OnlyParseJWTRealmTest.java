package com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.StatelessToken;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author xiangtch
 * @date 2019/9/12 12:56
 * <p> Email: xiangtiancheng@deepexi.com </p>
 */
public class OnlyParseJWTRealmTest {

    public static final String ISSUER = "test_token";

    private final PayloadTemplate payloadTemplate = new DefaultPayloadTemplate(ISSUER);
    JWTManager manager = new JWTManager();

    {
        payloadTemplate.addField("uid", Long.class);
        payloadTemplate.addField("username", String.class);
        payloadTemplate.addField("isAdmin", Boolean.class);
        manager.addPayloadTemplate(payloadTemplate);
    }

    @Test
    public void doGetAuthenticationInfoTest() throws Exception {
        Payload payload = new Payload();
        payload.put("uid", 12345L);
        payload.put("username", "tac");
        payload.put("isAdmin", true);
        String jwt = manager.create(ISSUER, payload);
        DecodedJWT decodedJWT = manager.verify(ISSUER, jwt);
        AuthenticationToken authenticationToken = new StatelessToken(decodedJWT.getToken());
        assertThat(new OnlyParseJWTRealm(ISSUER, manager).doGetAuthenticationInfo(authenticationToken))
                .isInstanceOf(AuthenticationInfo.class);
    }
}
