package com.github.taccisum.shiro.web.autoconfigure.stateless.integration;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import javax.servlet.Filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
    private WebApplicationContext webApplicationContext;
    private String token;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters((Filter) webApplicationContext.getBean(ShiroFilterFactoryBean.class).getObject())
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
    public void info() throws Exception {
        mvc.perform(get("/info")
                .header("token", token)
                .accept("application/json"))
                .andDo(print())
                .andExpect(jsonPath("$.username", is("cd6765734a16476b9bd4b0513b3fb8e4")))
        ;
    }

    @Test
    public void infoWhenUnauthenticated() throws Exception {
        mvc.perform(get("/info")
                .accept("application/json"))
                .andDo(print())
                .andExpect(content().string("unauthenticated user"))
        ;
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
                    .andExpect(jsonPath("$", is("staff")))
            ;
        } catch (NestedServletException e) {
            assertThat(e.getCause()).isInstanceOf(AuthorizationException.class);
            assertThat(e.getMessage()).contains("Subject does not have role [user]");
        }
    }

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
