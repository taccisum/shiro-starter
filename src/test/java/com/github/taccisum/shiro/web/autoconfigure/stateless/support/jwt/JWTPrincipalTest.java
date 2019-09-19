package com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author xiangtch - xiangtiancheng@deepexi.com
 * @since  2019/9/12 14:03
 */
public class JWTPrincipalTest {

    private static Payload payload = new Payload();
    static {
        payload.put("uid", 12345L);
        payload.put("username", "tac");
        payload.put("isAdmin", true);
    }

    private JWTPrincipal JWTPrincipal = new JWTPrincipal("token", payload);


    @Test
    public void onlyParseJWTPrincipalTest(){
        assertThat(JWTPrincipal.getToken()).isEqualTo("token");
        assertThat(JWTPrincipal.getPayload()).isEqualTo(payload);
    }
}
