package com.github.taccisum.shiro.web.autoconfigure.stateless.integration;

import com.github.taccisum.shiro.web.autoconfigure.session.integration.ShrioSessionModeApplication;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/3
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShrioSessionModeApplication.class)
@ActiveProfiles("stateless-mode")
@Ignore
public class TokenModeIntegrationTest {
}
