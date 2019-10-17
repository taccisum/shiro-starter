package com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.taccisum.shiro.web.autoconfigure.model.Model1;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt.exception.BuildPayloadException;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt.exception.ErrorFieldException;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt.exception.NotExistPayloadTemplateException;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt.exception.ParsePayloadException;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/6
 */
public class JWTManagerTest {
    public static final String ISSUER = "test_token";

    private PayloadTemplate payloadTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    private JWTManager manager = new JWTManager();

    private List<String> list = new ArrayList<>();
    private Map<String, String> map = new HashMap<>();
    private Set<String> set = new HashSet<>();
    private Model1 entity = new Model1();

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
        payloadTemplate.addField("list", List.class);
        payloadTemplate.addField("map", Map.class);
        payloadTemplate.addField("set", Set.class);
        payloadTemplate.addField("entity", Model1.class);
        manager.addPayloadTemplate(payloadTemplate);

        map.put("key1", "val1");
        list.add("val1");
        set.add("val1");
        entity.setValue1("val1");
        entity.setValue2("val2");
        entity.setValue3(null);
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
        payload.put("map", map);
        payload.put("list", list);
        payload.put("set", set);
        payload.put("entity", entity);
        return payload;
    }

    private String buildJWT(Payload payload) {
        return manager.create(ISSUER, payload);
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
        assertThat(objectMapper.writeValueAsString(pp.get("list"))).isEqualTo(objectMapper.writeValueAsString(list));
        assertThat(objectMapper.writeValueAsString(pp.get("map"))).isEqualTo(objectMapper.writeValueAsString(map));
        assertThat(objectMapper.writeValueAsString(pp.get("entity"))).isEqualTo(objectMapper.writeValueAsString(entity));

        // add for unit test
        Payload pp1 = manager.parsePayload(buildJWT(buildPayload()));
    }

    @Test
    public void createError() {
        Payload payload = new Payload();
        payload.put(null, "val1");
        assertThatThrownBy(() -> manager.create(ISSUER, payload)).isInstanceOf(BuildPayloadException.class);
        assertThatThrownBy(() -> manager.create("not exist", payload)).isInstanceOf(NotExistPayloadTemplateException.class);

        Payload payload1 = new Payload();
        payload1.put("list", new HashMap<>());
        assertThatThrownBy(() -> manager.create(ISSUER, payload1)).isInstanceOf(ErrorFieldException.class);

        assertThatThrownBy(() -> manager.parsePayload("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c", null)).isInstanceOf(NotExistPayloadTemplateException.class);
        assertThatThrownBy(() -> manager.parsePayload("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c", payloadTemplate)).isInstanceOf(ParsePayloadException.class);
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