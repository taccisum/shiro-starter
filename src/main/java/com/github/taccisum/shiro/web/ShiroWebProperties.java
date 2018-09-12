package com.github.taccisum.shiro.web;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/3
 */
@ConfigurationProperties("shiro.web")
public class ShiroWebProperties {
    private ShiroMode mode = ShiroMode.SESSION;
    private Boolean isRedirectEnabled = true;
    private Map<String, List<String>> filterChainDefinition = new LinkedHashMap<>();

    public ShiroMode getMode() {
        return mode;
    }

    public void setMode(ShiroMode mode) {
        this.mode = mode;
    }

    public Boolean getRedirectEnabled() {
        return isRedirectEnabled;
    }

    public void setRedirectEnabled(Boolean redirectEnabled) {
        isRedirectEnabled = redirectEnabled;
    }

    public Map<String, List<String>> getFilterChainDefinition() {
        return filterChainDefinition;
    }

    public void setFilterChainDefinition(Map<String, List<String>> filterChainDefinition) {
        this.filterChainDefinition = filterChainDefinition;
    }
}
