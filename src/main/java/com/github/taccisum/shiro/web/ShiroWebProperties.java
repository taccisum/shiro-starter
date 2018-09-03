package com.github.taccisum.shiro.web;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/3
 */
@ConfigurationProperties("shiro.web")
public class ShiroWebProperties {
    private ShiroMode mode = ShiroMode.SESSION;
    private RestProperties rest = new RestProperties();

    public ShiroMode getMode() {
        return mode;
    }

    public void setMode(ShiroMode mode) {
        this.mode = mode;
    }

    public RestProperties getRest() {
        return rest;
    }

    public void setRest(RestProperties rest) {
        this.rest = rest;
    }

    public static class RestProperties {
        private Boolean enabled = false;

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }
    }
}
