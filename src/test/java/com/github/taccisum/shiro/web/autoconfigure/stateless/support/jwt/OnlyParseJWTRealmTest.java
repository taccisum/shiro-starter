package com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author xiangtch
 * @date 2019/9/11 21:45
 * <p> Email: xiangtiancheng@deepexi.com </p>
 */
public class OnlyParseJWTRealmTest {

    @Test
    public void split() throws Exception {
        assertThat(OnlyParseJWTRealm.split("user,staff,baby")).contains("user", "staff", "baby");
    }
}
