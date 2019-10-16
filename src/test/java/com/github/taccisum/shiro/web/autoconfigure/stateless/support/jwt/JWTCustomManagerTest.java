package com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.taccisum.shiro.web.autoconfigure.model.Test;
import com.github.taccisum.shiro.web.autoconfigure.model.Test1;
import com.github.taccisum.shiro.web.autoconfigure.model.Test2;
import com.github.taccisum.shiro.web.autoconfigure.model.Test3;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt.exception.ErrorFieldException;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt.exception.NotExistPayloadTemplateException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

/**
 * Created By @author Zhouyuhang on 2019/10/15 17:11
 * <p></p>
 **/
public class JWTCustomManagerTest {

    private String issuer1 = "issuer1";
    private String issuer2 = "issuer2";
    private String issuer3 = "issuer3";
    // not add into payloadTemplates
    private String issuer4 = "issuer4";
    private JWTCustomManager jwtCustomManager = new JWTCustomManager();
    private JWTCustomManager jwtCustomManager1 = new JWTCustomManager(new DefaultJWTAlgorithmProvider());
    private Test test;
    private Test1 test1;
    private Test2 test2;
    // not add into payloadTemplates
    private Test3 test3;
    private ObjectMapper objectMapper = new ObjectMapper();

    {
        jwtCustomManager.addClaimsTemplate(issuer1, Test.class, issuer1);
        jwtCustomManager.addClaimsTemplate(issuer2, Test1.class, issuer2);
        jwtCustomManager.addClaimsTemplate(issuer3, Test2.class, issuer3);

        Test test = new Test();
        test.setValue1("value1");
        test.setValue2("value2");
        test.setValue3("value3");
        this.test = test;

        Test1 test11 = new Test1();
        Map<String, String> map = new HashMap<>();
        map.put("key1", "val1");
        map.put("key2", "val2");
        test11.setMap(map);
        test11.setValue1("value1");
        this.test1 = test11;

        Test2 test2 = new Test2();
        List<String> list = new ArrayList<>();
        list.add("val1");
        list.add("val2");
        test2.setList(list);
        test2.setValue1("value1");
        this.test2 = test2;
    }

    public String buildJWTIss1() {
        return jwtCustomManager.create(issuer1, test);
    }

    public String buildJWTIss2() {
        return jwtCustomManager.create(issuer2, test1);
    }

    public String buildJWTIss3() {
        return jwtCustomManager.create(issuer3, test2);
    }

    public String buildJWTIss4() {
        return jwtCustomManager.create(issuer4, test2);
    }

    public String buildJWTIss5() {
        return jwtCustomManager.create(issuer3, test3);
    }

    @org.junit.Test
    public void testCreate() {
        assertThat(buildJWTIss1()).isNotNull();
        assertThat(buildJWTIss2()).isNotNull();
        assertThat(buildJWTIss3()).isNotNull();

        assertThatThrownBy(this::buildJWTIss4).isInstanceOf(NotExistPayloadTemplateException.class)
                .hasMessage(issuer4);

        assertThatThrownBy(this::buildJWTIss5).isInstanceOf(ErrorFieldException.class)
                .hasMessage("claims-entity can not null");

        Test3 test31 = new Test3();
        test31.setValue3("val1");
        this.test3 = test31;

        assertThatThrownBy(this::buildJWTIss5).isInstanceOf(NotExistPayloadTemplateException.class)
                .hasMessage("claims-entity should equals the-issuer-payloadType");
    }

    @org.junit.Test
    public void parseClaim() {
        try {
            assertThat(objectMapper.writeValueAsString(jwtCustomManager.parseClaim(buildJWTIss1())))
                    .isEqualTo(objectMapper.writeValueAsString(test));
            assertThat(objectMapper.writeValueAsString(jwtCustomManager.parseClaim(buildJWTIss2())))
                    .isEqualTo(objectMapper.writeValueAsString(test1));
            assertThat(objectMapper.writeValueAsString(jwtCustomManager.parseClaim(buildJWTIss3())))
                    .isEqualTo(objectMapper.writeValueAsString(test2));

            assertThatThrownBy(() -> jwtCustomManager.parseClaim("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"))
                    .isInstanceOf(NotExistPayloadTemplateException.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
