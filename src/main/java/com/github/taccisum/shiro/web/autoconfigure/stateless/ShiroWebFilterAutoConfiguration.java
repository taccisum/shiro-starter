package com.github.taccisum.shiro.web.autoconfigure.stateless;

import com.github.taccisum.shiro.web.autoconfigure.AbstractShiroWebFilterAutoConfiguration;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.StatelessUserFilter;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/3
 */
@Configuration
@ConditionalOnProperty(name = "shiro.web.enabled", matchIfMissing = true)
public class ShiroWebFilterAutoConfiguration extends AbstractShiroWebFilterAutoConfiguration {
    private Logger logger = LoggerFactory.getLogger(ShiroWebFilterAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    @Override
    protected ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean filterFactoryBean = super.shiroFilterFactoryBean();

        logger.info("replace [authc] filter by " + StatelessUserFilter.class);
        filterFactoryBean.getFilters().put("authc", new StatelessUserFilter());

        return filterFactoryBean;
    }
}
