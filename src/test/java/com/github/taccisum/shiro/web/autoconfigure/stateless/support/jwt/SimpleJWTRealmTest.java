package com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/6
 */
public class SimpleJWTRealmTest {
    @Test
    public void split() throws Exception {
        assertThat(SimpleJWTRealm.split("user,staff,baby")).contains("user", "staff", "baby");
    }
}