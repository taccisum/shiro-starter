package com.github.taccisum.shiro.web.autoconfigure.stateless;

import com.github.taccisum.shiro.web.ShiroFilterDefinition;
import com.github.taccisum.shiro.web.ShiroWebProperties;
import com.github.taccisum.shiro.web.autoconfigure.AbstractShiroWebFilterAutoConfiguration;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.AuthOnNeedStatelessUserFilter;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.StatelessUserFilter;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.extractor.DefaultTokenExtractor;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.extractor.TokenExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ShiroWebFilterAutoConfiguration extends AbstractShiroWebFilterAutoConfiguration {
    private Logger logger = LoggerFactory.getLogger(ShiroWebFilterAutoConfiguration.class);
    @Autowired
    private ShiroWebProperties shiroWebProperties;

    @Bean
    @ConditionalOnMissingBean
    public TokenExtractor tokenExtractor() {
        return new DefaultTokenExtractor();
    }

    @Bean
    @ConditionalOnMissingBean
    public ShiroFilterDefinition shiroFilterDefinition(TokenExtractor tokenExtractor) {
        return filters -> {
            logger.info("replace [authc] filter by " + StatelessUserFilter.class);
            filters.put("authc", new StatelessUserFilter(shiroWebProperties, tokenExtractor));
            logger.info("add {} as [authn] filter ", AuthOnNeedStatelessUserFilter.class);
            filters.put("authn", new AuthOnNeedStatelessUserFilter(shiroWebProperties, tokenExtractor));
        };
    }
}
