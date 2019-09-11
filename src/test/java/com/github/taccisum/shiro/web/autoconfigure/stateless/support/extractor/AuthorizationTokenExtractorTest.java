package com.github.taccisum.shiro.web.autoconfigure.stateless.support.extractor;

import com.github.taccisum.shiro.web.autoconfigure.stateless.integration.ShiroStatelessModeApplication;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.StatelessUserFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author xiangtch
 * @date 2019/9/11 19:13
 * <p> Email: xiangtiancheng@deepexi.com </p>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShiroStatelessModeApplication.class)
public class AuthorizationTokenExtractorTest {


    @Test
    public void getTokenTest(){
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.setParameter("Authorization", "authorization");
        AuthorizationTokenExtractor authorizationTokenExtractor = new AuthorizationTokenExtractor();
        assertThat(authorizationTokenExtractor.getToken(mockHttpServletRequest)).isEqualTo("authorization");
    }
}
