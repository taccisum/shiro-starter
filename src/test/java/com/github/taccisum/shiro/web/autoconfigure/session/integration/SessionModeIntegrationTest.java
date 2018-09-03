package com.github.taccisum.shiro.web.autoconfigure.session.integration;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import javax.servlet.Filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/3
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShrioSessionModeApplication.class)
@ActiveProfiles("session-mode")
public class SessionModeIntegrationTest {
    private MockMvc mvc;
    private MockHttpSession session;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception {
        this.mvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .addFilters((Filter) webApplicationContext.getBean(ShiroFilterFactoryBean.class).getObject())
                .build();
        this.session = new MockHttpSession();
        MvcResult result = doLogin()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)))
                .andReturn();
        assertThat(result.getResponse().getHeader("Set-Cookie")).isNotEmpty().contains("rememberMe", "Max-Age", "Expires");
    }

    @Test
    public void testSimply() throws Exception {
        assertThat(mvc).isNotNull();
    }

    @Test
    public void loginRepeatedly() throws Exception {
        mvc.perform(get("/login")
                .accept("application/json")
                .session(session))
                .andDo(print())
                .andExpect(jsonPath("$", is(false)))
        ;
    }

    @Test
    public void logout() throws Exception {
        mvc.perform(get("/logout")
                .accept("application/json")
                .session(session))
                .andDo(print())
                .andExpect(jsonPath("$", is(true)))
        ;
    }

    @Test
    public void annotation() throws Exception {
        mvc.perform(get("/require_staff")
                .accept("application/json")
                .session(session))
                .andDo(print())
                .andExpect(jsonPath("$", is("staff")))
        ;
        try {
            mvc.perform(get("/require_user")
                    .accept("application/json")
                    .session(session))
                    .andDo(print())
            ;
        } catch (NestedServletException e) {
            assertThat(e.getCause()).isInstanceOf(AuthorizationException.class);
            assertThat(e.getMessage()).contains("Subject does not have role [user]");
        }
    }

    private ResultActions doLogin() throws Exception {
        return mvc.perform(get("/login")
                .accept("application/json")
                .session(session));
    }
}
