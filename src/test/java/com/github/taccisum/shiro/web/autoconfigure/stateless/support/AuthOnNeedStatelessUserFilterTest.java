package com.github.taccisum.shiro.web.autoconfigure.stateless.support;

import com.github.taccisum.shiro.web.autoconfigure.stateless.support.extractor.TokenExtractor;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author taccisum - liaojinfeng6938@dingtalk.com
 * @since 2021-06-21
 */
public class AuthOnNeedStatelessUserFilterTest {
    @Test
    public void login() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        TokenExtractor extractor = mock(TokenExtractor.class);
        AuthOnNeedStatelessUserFilter filter = new AuthOnNeedStatelessUserFilter(null, extractor);

        when(extractor.getToken(any())).thenReturn("");
        assertThat(filter.login(request)).isTrue();
        when(extractor.getToken(any())).thenReturn(null);
        assertThat(filter.login(request)).isTrue();
        when(extractor.getToken(any())).thenReturn("  ");
        assertThat(filter.login(request)).isTrue();
    }
}