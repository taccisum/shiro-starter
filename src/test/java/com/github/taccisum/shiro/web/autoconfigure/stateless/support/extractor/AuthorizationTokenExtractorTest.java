package com.github.taccisum.shiro.web.autoconfigure.stateless.support.extractor;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author xiangtch - xiangtiancheng@deepexi.com
 * @since  2019/9/12 15:08npm
 */
public class AuthorizationTokenExtractorTest {

    @Test
    public void getTokenTest(){
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.setParameter("Authorization", "authorization");
        AuthorizationTokenExtractor authorizationTokenExtractor = new AuthorizationTokenExtractor();
        assertThat(authorizationTokenExtractor.getToken(mockHttpServletRequest)).isEqualTo("authorization");
    }

    @Test
    public void getTokenOnTokenStartWithBearer(){
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.setParameter("Authorization", "Bearer 123");
        AuthorizationTokenExtractor authorizationTokenExtractor = new AuthorizationTokenExtractor();
        assertThat(authorizationTokenExtractor.getToken(mockHttpServletRequest)).isEqualTo("123");
    }
}
