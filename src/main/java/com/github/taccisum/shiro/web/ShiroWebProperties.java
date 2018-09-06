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
    private Map<String, List<String>> filterChainDefinition = new LinkedHashMap<>();
    private StatelessProperties stateless = new StatelessProperties();

    public ShiroMode getMode() {
        return mode;
    }

    public void setMode(ShiroMode mode) {
        this.mode = mode;
    }

    public Map<String, List<String>> getFilterChainDefinition() {
        return filterChainDefinition;
    }

    public void setFilterChainDefinition(Map<String, List<String>> filterChainDefinition) {
        this.filterChainDefinition = filterChainDefinition;
    }

    public StatelessProperties getStateless() {
        return stateless;
    }

    public void setStateless(StatelessProperties stateless) {
        this.stateless = stateless;
    }

    public static class StatelessProperties {
        private JWTProperties jwt = new JWTProperties();

        public JWTProperties getJwt() {
            return jwt;
        }

        public void setJwt(JWTProperties jwt) {
            this.jwt = jwt;
        }

        public static class JWTProperties {
            private String issuer = "access_token";
            private Integer expires = 60 * 24;

            public String getIssuer() {
                return issuer;
            }

            public void setIssuer(String issuer) {
                this.issuer = issuer;
            }

            public Integer getExpires() {
                return expires;
            }

            public void setExpires(Integer expires) {
                this.expires = expires;
            }
        }
    }
}
