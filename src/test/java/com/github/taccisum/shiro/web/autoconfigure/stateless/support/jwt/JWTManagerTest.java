package com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.taccisum.shiro.web.autoconfigure.model.Model1;
import com.github.taccisum.shiro.web.autoconfigure.model.Model2;
import com.github.taccisum.shiro.web.autoconfigure.model.Model3;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt.exception.NotExistPayloadTemplateException;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/6
 */
public class JWTManagerTest {
    public static final String ISSUER = "test_token";
    public static final String ISSUER1 = "issuer1";
    public static final String ISSUER2 = "issuer2";
    public static final String ISSUER3 = "issuer3";

    // prepared payloadTemplate for initial
    private PayloadTemplate payloadTemplate;
    private PayloadTemplate payloadTemplate1;
    private PayloadTemplate payloadTemplate2;
    private PayloadTemplate payloadTemplate3;

    // prepared entity for building PayloadTemplate and add fields for creating JWT
    private Model1 model1;
    private Model2 model2;
    private Model3 model3;

    // prepared payload for parse JWT
    private Payload payload1;
    private Payload payload2;
    private Payload payload3;

    private ObjectMapper objectMapper = new ObjectMapper();
    private JWTManager manager = new JWTManager();

    {
        // add payloadTemplate into maps for initial
        payloadTemplate = new DefaultPayloadTemplate(ISSUER);
        payloadTemplate.addField("uid", Long.class);
        payloadTemplate.addField("username", String.class);
        payloadTemplate.addField("isAdmin", Boolean.class);
        payloadTemplate.addField("age", Integer.class);
        payloadTemplate.addField("createTime", Date.class);
        payloadTemplate.addField("value", Double.class);
        payloadTemplate.addField("float", Float.class);
        manager.addPayloadTemplate(payloadTemplate);

        payloadTemplate1 = new DefaultPayloadTemplate(ISSUER1);
        payloadTemplate1.addField(ISSUER1, Model1.class);
        manager.addPayloadTemplate(payloadTemplate1);

        payloadTemplate2 = new DefaultPayloadTemplate(ISSUER2);
        payloadTemplate2.addField(ISSUER2, Model2.class);
        manager.addPayloadTemplate(payloadTemplate2);

        payloadTemplate3 = new DefaultPayloadTemplate(ISSUER3);
        payloadTemplate3.addField(ISSUER3, Model3.class);
        manager.addPayloadTemplate(payloadTemplate3);

        // create entity and add filed for creating JWT
        Model1 model1 = new Model1();
        model1.setValue1("val1");
        model1.setValue2("val2");
        model1.setValue3("val3");
        this.model1 = model1;

        Map<String, String> map = new HashMap<>();
        map.put("key1", "val1");
        map.put("key2", "val2");
        Model2 model2 = new Model2();
        model2.setMap(map);
        model2.setValue1("val1");
        this.model2 = model2;

        List<String> list = new ArrayList<>();
        list.add("val1");
        list.add("val2");
        Model3 model3 = new Model3();
        model3.setList(list);
        model3.setValue1("val1");
        this.model3 = model3;

        // create payload for creating JWT
        this.payload1 = new Payload();
        payload1.put(ISSUER1, model1);

        this.payload2 = new Payload();
        payload2.put(ISSUER2, model2);

        this.payload3 = new Payload();
        payload3.put(ISSUER3, model3);
    }

    private Payload buildPayload() {
        Payload payload = new Payload();
        payload.put("uid", 12345678900L);
        payload.put("username", "tac");
        payload.put("isAdmin", true);
        payload.put("age", 18);
        final Date createTime = new Date();
        payload.put("createTime", createTime);
        payload.put("value", 3.5);
        payload.put("float", 3.5f);
        return payload;
    }

    private String buildJWT(Payload payload) {
        return manager.create(ISSUER, payload);
    }
    private String buildJWT1() {
        return manager.create(ISSUER1, payload1);
    }
    private String buildJWT2() {
        return manager.create(ISSUER2, payload2);
    }
    private String buildJWT3() {
        return manager.create(ISSUER3, payload3);
    }

    private DecodedJWT buildDecodeJWT(String jwt, String issuer) {
        return manager.verify(issuer, jwt);
    }

    @Test
    public void create() throws Exception {
        Payload pp = manager.parsePayload(buildDecodeJWT(buildJWT(buildPayload()), ISSUER));
        assertThat((Long) pp.get("uid")).isEqualTo(12345678900L);
        assertThat((String) pp.get("username")).isEqualTo("tac");
        assertThat((Boolean) pp.get("isAdmin")).isEqualTo(true);
        assertThat((Integer) pp.get("age")).isEqualTo(18);
        assertThat((Date) pp.get("createTime")).isInstanceOf(Date.class);
        assertThat((Double) pp.get("value")).isEqualTo(3.5);
    }

    @Test
    public void createEntity() throws Exception {
        Payload payload1 = manager.parsePayload(buildJWT1());
        assertThat(objectMapper.writeValueAsString(payload1.get(ISSUER1)))
                .isEqualTo(objectMapper.writeValueAsString(model1));

        Payload payload2 = manager.parsePayload(buildDecodeJWT(buildJWT2(), ISSUER2));
        assertThat(objectMapper.writeValueAsString(payload2.get(ISSUER2)))
                .isEqualTo(objectMapper.writeValueAsString(model2));

        Payload payload3 = manager.parsePayload(buildDecodeJWT(buildJWT3(), ISSUER3));
        assertThat(objectMapper.writeValueAsString(payload3.get(ISSUER3)))
                .isEqualTo(objectMapper.writeValueAsString(model3));
    }


    @Test
    public void parsePayloadTestWithNoPayloadTemplate() {
        try {
            manager.parsePayload(buildDecodeJWT(buildJWT(buildPayload()), ISSUER), null);
        } catch (Exception e) {
            assertThat(e).hasMessage(ISSUER);
        }
    }

    @Test
    public void verifyAndParsePayloadTest() {
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

    @Test
    public void putAll() {
        Map<String, String> map = new HashMap<>();
        map.put("k1","v1");
        map.put("k2","v2");
        Payload payload = new Payload();
        payload.putAll(map);
    }
}