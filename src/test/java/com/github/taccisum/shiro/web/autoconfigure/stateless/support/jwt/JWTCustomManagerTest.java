package com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.taccisum.shiro.web.autoconfigure.model.Test;
import com.github.taccisum.shiro.web.autoconfigure.model.Test1;
import com.github.taccisum.shiro.web.autoconfigure.model.Test2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created By @author Zhouyuhang on 2019/10/15 17:11
 * <p></p>
 **/
public class JWTCustomManagerTest {

    private String issuer1 = "issuer1";
    private String issuer2 = "issuer2";
    private String issuer3 = "issuer3";
    private JWTCustomManager jwtCustomManager = new JWTCustomManager();
    private Test test;
    private Test1 test1;
    private Test2 test2;
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

    @org.junit.Test
    public void create() {
        System.out.println(buildJWTIss1());
        System.out.println(buildJWTIss2());
        System.out.println(buildJWTIss3());
    }

    @org.junit.Test
    public void parseClaim() {
        try {
            System.out.println(objectMapper.writeValueAsString(jwtCustomManager.parseClaim(buildJWTIss1())));
            System.out.println(objectMapper.writeValueAsString(jwtCustomManager.parseClaim(buildJWTIss2())));
            System.out.println(objectMapper.writeValueAsString(jwtCustomManager.parseClaim(buildJWTIss3())));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
