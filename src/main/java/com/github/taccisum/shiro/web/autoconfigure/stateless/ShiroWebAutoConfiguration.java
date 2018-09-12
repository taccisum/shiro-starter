package com.github.taccisum.shiro.web.autoconfigure.stateless;

import com.github.taccisum.shiro.web.ShiroWebProperties;
import com.github.taccisum.shiro.web.autoconfigure.AbstractShiroWebAutoConfiguration;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.StatelessSessionStorageEvaluator;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.StatelessSubjectFactory;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt.*;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SessionStorageEvaluator;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/3
 */
@Configuration
@ConditionalOnProperty(name = "shiro.web.mode", havingValue = "STATELESS")
public class ShiroWebAutoConfiguration extends AbstractShiroWebAutoConfiguration {
    public static final String DEFAULT_ISSUER = "access_token";
    private Logger logger = LoggerFactory.getLogger(ShiroWebAutoConfiguration.class);

    @Autowired
    private ShiroWebProperties shiroWebProperties;

    @Autowired(required = false)
    private List<PayloadTemplate> payloadTemplates;

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
    protected JWTManager jwtManager(JWTAlgorithmProvider algorithmProvider) {
        JWTManager jwtManager = new JWTManager(algorithmProvider);
        if (CollectionUtils.isEmpty(payloadTemplates)) {
            logger.info(String.format("not found any payload template bean. add default payload template for issuer \"%s\"", DEFAULT_ISSUER));
            PayloadTemplate template = new DefaultPayloadTemplate(DEFAULT_ISSUER);
            template.addField("uid", Long.class);
            template.addField("username", String.class);
            template.addField("roles", String.class);
            template.addField("permissions", String.class);
            jwtManager.addPayloadTemplate(template);
        } else {
            for (PayloadTemplate template : payloadTemplates) {
                jwtManager.addPayloadTemplate(template);
            }
        }

        return jwtManager;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(name = "com.auth0.jwt.JWT")
    protected JWTAlgorithmProvider jwtAlgorithmProvider() {
        return new DefaultJWTAlgorithmProvider();
    }
}
