package com.github.taccisum.shiro.web.autoconfigure;

import com.github.taccisum.shiro.web.ShiroWebProperties;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.AbstractShiroWebFilterConfiguration;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/3
 */
public class AbstractShiroWebFilterAutoConfiguration extends AbstractShiroWebFilterConfiguration {
    @Bean
    @ConditionalOnMissingBean
    @Override
    protected ShiroFilterFactoryBean shiroFilterFactoryBean() {
        return super.shiroFilterFactoryBean();
    }

    @Bean(name = "filterShiroFilterRegistrationBean")
    @ConditionalOnMissingBean
    protected FilterRegistrationBean filterShiroFilterRegistrationBean() throws Exception {

        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter((AbstractShiroFilter) shiroFilterFactoryBean().getObject());
        filterRegistrationBean.setOrder(1);

        return filterRegistrationBean;
    }

    @Bean
    @ConditionalOnMissingBean
    public ShiroFilterChainDefinition shiroFilterChainDefinition(ShiroWebProperties shiroWebProperties) {
        OrderedShiroFilterChainDefinition chainDefinition = new OrderedShiroFilterChainDefinition();
        shiroWebProperties.getFilterChainDefinition().forEach((filter, paths) -> {
            for (String antPath : paths) {
                chainDefinition.addPathDefinition(antPath, filter);
            }
        });
        return chainDefinition;
    }

    private class OrderedShiroFilterChainDefinition implements ShiroFilterChainDefinition {
        final private Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        public void addPathDefinition(String antPath, String definition) {
            filterChainDefinitionMap.put(antPath, definition);
        }

        public void addPathDefinitions(Map<String, String> pathDefinitions) {
            filterChainDefinitionMap.putAll(pathDefinitions);
        }

        @Override
        public Map<String, String> getFilterChainMap() {
            return filterChainDefinitionMap;
        }
    }
}
