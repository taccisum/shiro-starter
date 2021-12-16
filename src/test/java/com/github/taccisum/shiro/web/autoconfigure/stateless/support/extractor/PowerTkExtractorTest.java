package com.github.taccisum.shiro.web.autoconfigure.stateless.support.extractor;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author taccisum - liaojinfeng6938@dingtalk.com
 * @since 2021-12-16
 */
public class PowerTkExtractorTest {
    public static final String TK_NAME = "tk";
    private PowerTkExtractor extractor;
    private HttpServletRequest req;

    @Before
    public void setUp() throws Exception {
        extractor = new PowerTkExtractor("tk");
        req = mock(HttpServletRequest.class);
    }

    @Test
    public void getFromHeaderAuthorization() {
        when(req.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("tk_str");
        assertThat(extractor.getToken(req)).isEqualTo("tk_str");
    }

    @Test
    public void getFromCookies() {
        when(req.getCookies()).thenReturn(new Cookie[]{
                new Cookie("tk", "tk_str")
        });
        assertThat(extractor.getToken(req)).isEqualTo("tk_str");
    }

    @Test
    public void getFromQueryString() {
        when(req.getParameter(TK_NAME)).thenReturn("tk_str");
        assertThat(extractor.getToken(req)).isEqualTo("tk_str");
    }

    @Test
    public void getFromSecondTkName() {
        final String SECOND_TK_NAME = "second_tk_name";
        PowerTkExtractor extractor = new PowerTkExtractor(TK_NAME, SECOND_TK_NAME);

        when(req.getParameter(TK_NAME)).thenReturn("tk_str");
        when(req.getParameter(SECOND_TK_NAME)).thenReturn("second_tk_str");

        // first tk 优先级更高
        assertThat(extractor.getToken(req)).isEqualTo("tk_str");

        // first tk 不存在，则取 second tk
        when(req.getParameter(TK_NAME)).thenReturn(null);
        assertThat(extractor.getToken(req)).isEqualTo("second_tk_str");
    }

    @Test
    public void checkPriority() {
        when(req.getParameter(TK_NAME)).thenReturn("qs_tk_str");
        assertThat(extractor.getToken(req)).isEqualTo("qs_tk_str");
        when(req.getCookies()).thenReturn(new Cookie[]{
                new Cookie("tk", "ck_tk_str")
        });
        assertThat(extractor.getToken(req)).isEqualTo("ck_tk_str");
        when(req.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("header_auth_tk_str");
        assertThat(extractor.getToken(req)).isEqualTo("header_auth_tk_str");
        extractor.setMock(true);
        extractor.setMockTk("mock_tk_str");
        assertThat(extractor.getToken(req)).isEqualTo("mock_tk_str");
    }
}
