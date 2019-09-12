package com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author xiangtch
 * @date 2019/9/12 14:03
 * <p> Email: xiangtiancheng@deepexi.com </p>
 */
public class OnlyParseJWTPrincipalTest {

    private static Payload payload = new Payload();
    static {
        payload.put("uid", 12345L);
        payload.put("username", "tac");
        payload.put("isAdmin", true);
    }

    private  OnlyParseJWTPrincipal onlyParseJWTPrincipal = new OnlyParseJWTPrincipal("token", payload);


    @Test
    public void onlyParseJWTPrincipalTest(){
        assertThat(onlyParseJWTPrincipal.getToken()).isEqualTo("token");
        assertThat(onlyParseJWTPrincipal.getPayload()).isEqualTo(payload);
    }
}
