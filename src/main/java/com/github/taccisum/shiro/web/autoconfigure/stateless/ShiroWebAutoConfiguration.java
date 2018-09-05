package com.github.taccisum.shiro.web.autoconfigure.stateless;

import com.github.taccisum.shiro.web.autoconfigure.AbstractShiroWebAutoConfiguration;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.StatelessSessionStorageEvaluator;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.StatelessSubjectFactory;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SessionStorageEvaluator;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/3
 */
@Configuration
@ConditionalOnProperty(name = "shiro.web.mode", havingValue = "STATELESS")
public class ShiroWebAutoConfiguration extends AbstractShiroWebAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    @Override
    protected SessionStorageEvaluator sessionStorageEvaluator() {
        return new StatelessSessionStorageEvaluator();
    }

    @Bean
    @ConditionalOnMissingBean
    @Override
    protected SessionManager sessionManager() {
        SessionManager sessionManager = super.sessionManager();
        if (sessionManager instanceof DefaultWebSessionManager) {
            ((DefaultWebSessionManager) sessionManager).setSessionIdCookieEnabled(false);
        }
        return sessionManager;
    }

    @Bean
    @ConditionalOnMissingBean(name = "sessionCookieTemplate")
    @Override
    protected Cookie sessionCookieTemplate() {
        return null;
    }

    @Bean
    @ConditionalOnMissingBean
    @Override
    protected RememberMeManager rememberMeManager() {
        return null;
    }

    @Bean
    @ConditionalOnMissingBean(name = "rememberMeCookieTemplate")
    @Override
    protected Cookie rememberMeCookieTemplate() {
        return null;
    }

    @Bean
    @ConditionalOnMissingBean
    @Override
    protected SubjectFactory subjectFactory() {
        return new StatelessSubjectFactory();
    }
}
