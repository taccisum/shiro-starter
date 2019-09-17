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
        payloadTemplate.addField("age", Integer.class);
        payloadTemplate.addField("createTime", Date.class);
        payloadTemplate.addField("value", Double.class);
        payloadTemplate.addField("float", Float.class);
        manager.addPayloadTemplate(payloadTemplate);
    }

    private Payload buildPayload(){
        Payload payload = new Payload();
        payload.put("uid", 12345678900L);
        payload.put("username", "tac");
        payload.put("isAdmin", true);
        payload.put("age", 18);
        final Date createTime = new Date();
        payload.put("createTime",createTime);
        payload.put("value", 3.5);
        payload.put("float", 3.5f);
        return payload;
    }

    private String buildJWT(Payload payload){
        return manager.create(ISSUER, payload);
    }

    private DecodedJWT buildDecodeJWT(String jwt){
        return manager.verify(ISSUER, jwt);
    }

    @Test
    public void create() throws Exception {
        Payload pp = manager.parsePayload(buildDecodeJWT(buildJWT(buildPayload())));
        assertThat((Long) pp.get("uid")).isEqualTo(12345678900L);
        assertThat((String) pp.get("username")).isEqualTo("tac");
        assertThat((Boolean) pp.get("isAdmin")).isEqualTo(true);
        assertThat((Integer) pp.get("age")).isEqualTo(18);
        assertThat((Date) pp.get("createTime")).isInstanceOf(Date.class);
        assertThat((Double) pp.get("value")).isEqualTo(3.5);
    }

    @Test
    public void parsePayloadTestWithNoPayloadTemplate(){
        try {
            manager.parsePayload(buildDecodeJWT(buildJWT(buildPayload())), null);
        }catch (Exception e){
            assertThat(e).hasMessage(ISSUER);
        }
    }

    @Test
    public void verifyAndParsePayloadTest(){
        assertThat(manager.verifyAndParsePayload(ISSUER, buildJWT(buildPayload()))).isNotEmpty();
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
            assertThat(Math.abs(JWTManager.calculateExpiresTime(EXPIRES_MINUTES).getTime() - new Date().getTime() - EXPIRES_MINUTES * 60 * 1000)).isLessThan(100);
        }
    }

}