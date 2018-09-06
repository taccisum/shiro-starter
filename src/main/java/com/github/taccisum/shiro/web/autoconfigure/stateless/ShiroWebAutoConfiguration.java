package com.github.taccisum.shiro.web.autoconfigure.stateless;

import com.github.taccisum.shiro.web.ShiroWebProperties;
import com.github.taccisum.shiro.web.autoconfigure.AbstractShiroWebAutoConfiguration;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.StatelessSessionStorageEvaluator;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.StatelessSubjectFactory;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt.DefaultJWTAlgorithmProvider;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt.JWTAlgorithmProvider;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt.JWTManager;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt.PayloadTemplate;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SessionStorageEvaluator;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
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
    @Autowired
    private ShiroWebProperties shiroWebProperties;

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

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(name = "com.auth0.jwt.JWT")
    protected JWTManager jwtManager(PayloadTemplate payloadTemplate, JWTAlgorithmProvider algorithmProvider) {
        ShiroWebProperties.StatelessProperties.JWTProperties jwtProperties = shiroWebProperties.getStateless().getJwt();
        return new JWTManager(jwtProperties.getIssuer(), payloadTemplate, jwtProperties.getExpires(), algorithmProvider);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(name = "com.auth0.jwt.JWT")
    protected PayloadTemplate payloadTemplate() {
        PayloadTemplate template = new PayloadTemplate();
        template.addField("uid", Long.class);
        template.addField("username", String.class);
        template.addField("roles", String.class);
        template.addField("permissions", String.class);
        return template;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(name = "com.auth0.jwt.JWT")
    protected JWTAlgorithmProvider jwtAlgorithmProvider() {
        return new DefaultJWTAlgorithmProvider();
    }
}
