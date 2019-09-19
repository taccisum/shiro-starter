package com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.taccisum.shiro.web.autoconfigure.stateless.integration.ShiroStatelessModeApplication;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.StatelessToken;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

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
