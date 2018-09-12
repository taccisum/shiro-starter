package com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt.exception.NotExistPayloadTemplateException;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/6
 */
public class JWTManagerTest {
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
    public void create() throws Exception {
        Payload payload = new Payload();
        payload.put("uid", 12345L);
        payload.put("username", "tac");
        payload.put("isAdmin", true);
        String jwt = manager.create(ISSUER, payload);
        System.out.println(jwt);
        assertThat(jwt).isNotEmpty();

        DecodedJWT decodedJWT = manager.verify(ISSUER, jwt);
        Payload pp = manager.parsePayload(ISSUER, decodedJWT);
        assertThat((Long) pp.get("uid")).isEqualTo(12345L);
        assertThat((String) pp.get("username")).isEqualTo("tac");
        assertThat((Boolean) pp.get("isAdmin")).isEqualTo(true);
    }

    // todo::
    @Test(expected = NotExistPayloadTemplateException.class)
    @Ignore
    public void createWhenPayloadTemplateNotExist() throws Exception {
    }

    @Test
    public void calculateExpiresTime() throws Exception {
        final int EXPIRES_MINUTES = 30;
        for (int i = 0; i < 1000; i++) {
            assertThat(Math.abs(JWTManager.calculateExpiresTime(EXPIRES_MINUTES).getTime() - new Date().getTime() - EXPIRES_MINUTES * 60 * 1000)).isLessThan(10);
        }
    }
}