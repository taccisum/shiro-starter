package com.github.taccisum.shiro.web.autoconfigure.stateless.integration;

import com.github.taccisum.shiro.web.autoconfigure.stateless.support.StatelessCredentialsMatcher;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.StatelessSessionStorageEvaluator;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.StatelessSubjectFactory;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.StatelessUserFilter;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SessionStorageEvaluator;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import javax.servlet.Filter;
import javax.servlet.ServletException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/3
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShiroStatelessModeApplication.class)
@ActiveProfiles("stateless-mode")
public class StatelessModeIntegrationTest {
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext context;
    private String token;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters((Filter) context.getBean(ShiroFilterFactoryBean.class).getObject())
                .build();
        token = login();
    }

    @Test
    public void testSimply() throws Exception {
        assertThat(mvc).isNotNull();
        assertThat(token).isNotEmpty();
        assertThat(token.length()).isEqualTo(32);
    }

    @Test
    public void testBean() throws Exception {
        assertThat(context.getBean(SessionStorageEvaluator.class)).isInstanceOf(StatelessSessionStorageEvaluator.class);
        SessionManager sessionManager = (SessionManager) context.getBean("sessionManager");
        if (sessionManager instanceof DefaultWebSessionManager) {
            assertThat(((DefaultWebSessionManager) sessionManager).isSessionIdCookieEnabled()).isFalse();
        }
        assertThat((Cookie) context.getBean("sessionCookieTemplate")).isNull();
        assertThat(context.getBean(RememberMeManager.class)).isNull();
        assertThat((Cookie) context.getBean("rememberMeCookieTemplate")).isNull();
        assertThat(((AuthorizingRealm) context.getBean(Realm.class)).getCredentialsMatcher()).isInstanceOf(StatelessCredentialsMatcher.class);
        assertThat(context.getBean(SubjectFactory.class)).isInstanceOf(StatelessSubjectFactory.class);
        assertThat(context.getBean(ShiroFilterFactoryBean.class).getFilters().get("authc")).isInstanceOf(StatelessUserFilter.class);
    }

    @Test
    public void info() throws Exception {
        MvcResult result = mvc.perform(get("/info")
                .header("token", token)
                .accept("application/json"))
                .andDo(print())
                .andExpect(jsonPath("$.username", is("cd6765734a16476b9bd4b0513b3fb8e4")))
                .andReturn();
        assertThat(result.getResponse().getHeader("Set-Cookie")).isNullOrEmpty();
    }

    @Test
    public void infoWhenUnauthenticated() throws Exception {
        try {
            mvc.perform(get("/info")
                    .accept("application/json"))
                    .andDo(print())
            ;
        } catch (ServletException e) {
            assertThat(e.getMessage().contains("unauthenticated user"));
        }
    }

    @Test
    public void infoWhenErrorToken() throws Exception {
        try {
            mvc.perform(get("/info")
                    .header("token", "error_token")
                    .accept("application/json"))
                    .andDo(print())
            ;
        } catch (ServletException e) {
            assertThat(e.getCause()).isInstanceOf(UnknownAccountException.class);
        }
    }

    @Test
    public void staff() throws Exception {
        mvc.perform(get("/require_staff")
                .header("token", token)
                .accept("application/json"))
                .andDo(print())
                .andExpect(jsonPath("$", is("staff")))
        ;
    }

    @Test
    public void user() throws Exception {
        try {
            mvc.perform(get("/require_user")
                    .header("token", token)
                    .accept("application/json"))
                    .andDo(print())
            ;
            Assert.fail();
        } catch (NestedServletException e) {
            assertThat(e.getCause()).isInstanceOf(AuthorizationException.class);
            assertThat(e.getMessage()).contains("Subject does not have role [user]");
        }
    }

//    @Test
//    public void userAuthorizationToken() throws Exception {
//        try {
//            mvc.perform(get("/require_user")
//                    .header("Authorization", token)
//                    .accept("application/json"))
//                    .andDo(print())
//            ;
//            Assert.fail();
//        } catch (NestedServletException e) {
//            assertThat(e.getCause()).isInstanceOf(AuthorizationException.class);
//            assertThat(e.getMessage()).contains("Subject does not have role [user]");
//        }
//    }

    @Test
    public void assertSessionNull() throws Exception {
        mvc.perform(get("/assert_session_null")
                .header("token", token)
                .accept("application/json"))
                .andDo(print())
                .andExpect(jsonPath("$", is(true)))
        ;
    }

    public String login() throws Exception {
        return mvc.perform(get("/login")
                .accept("application/json"))
                .andDo(print())
                .andReturn()
                .getResponse().getContentAsString()
                ;
    }
}
