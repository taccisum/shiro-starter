package com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created By @author Zhouyuhang on 2019/10/15 17:11
 * <p></p>
 **/
public class MultiJWTRealmTest {

    @Test
    public void testMultiJWTRealm() {
        JWTCustomManager jwtCustomManager = new JWTCustomManager();
        assertThat(new MultiJWTRealm("issuer1", jwtCustomManager))
                .isInstanceOf(MultiJWTRealm.class);
    }
}
