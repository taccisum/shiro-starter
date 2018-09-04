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
    private Boolean enabled = true;
    private ShiroMode mode = ShiroMode.SESSION;
    private AnnotationsProperties annotations = new AnnotationsProperties();
    private SessionManagerProperties sessionManager = new SessionManagerProperties();
    private RememberMeManagerProperties rememberMeManager = new RememberMeManagerProperties();
    private Map<String, List<String>> filterChainDefinition = new LinkedHashMap<>();

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public ShiroMode getMode() {
        return mode;
    }

    public void setMode(ShiroMode mode) {
        this.mode = mode;
    }

    public AnnotationsProperties getAnnotations() {
        return annotations;
    }

    public void setAnnotations(AnnotationsProperties annotations) {
        this.annotations = annotations;
    }

    public SessionManagerProperties getSessionManager() {
        return sessionManager;
    }

    public void setSessionManager(SessionManagerProperties sessionManager) {
        this.sessionManager = sessionManager;
    }

    public RememberMeManagerProperties getRememberMeManager() {
        return rememberMeManager;
    }

    public void setRememberMeManager(RememberMeManagerProperties rememberMeManager) {
        this.rememberMeManager = rememberMeManager;
    }

    public Map<String, List<String>> getFilterChainDefinition() {
        return filterChainDefinition;
    }

    public void setFilterChainDefinition(Map<String, List<String>> filterChainDefinition) {
        this.filterChainDefinition = filterChainDefinition;
    }

    public static class AnnotationsProperties {
        private Boolean enabled;

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }
    }

    public static class SessionManagerProperties {
        private Boolean deleteInvalidSessions;
        private Boolean sessionIdCookieEnabled;
        private Boolean sessionIdUrlRewritingEnabled;
        private CookieProperties cookie = new CookieProperties();

        public Boolean getDeleteInvalidSessions() {
            return deleteInvalidSessions;
        }

        public void setDeleteInvalidSessions(Boolean deleteInvalidSessions) {
            this.deleteInvalidSessions = deleteInvalidSessions;
        }

        public Boolean getSessionIdCookieEnabled() {
            return sessionIdCookieEnabled;
        }

        public void setSessionIdCookieEnabled(Boolean sessionIdCookieEnabled) {
            this.sessionIdCookieEnabled = sessionIdCookieEnabled;
        }

        public Boolean getSessionIdUrlRewritingEnabled() {
            return sessionIdUrlRewritingEnabled;
        }

        public void setSessionIdUrlRewritingEnabled(Boolean sessionIdUrlRewritingEnabled) {
            this.sessionIdUrlRewritingEnabled = sessionIdUrlRewritingEnabled;
        }

        public CookieProperties getCookie() {
            return cookie;
        }

        public void setCookie(CookieProperties cookie) {
            this.cookie = cookie;
        }
    }

    public static class RememberMeManagerProperties {
        private CookieProperties cookie = new CookieProperties();

        public CookieProperties getCookie() {
            return cookie;
        }

        public void setCookie(CookieProperties cookie) {
            this.cookie = cookie;
        }
    }

    public static class CookieProperties {
        private String name;
        private Integer maxAge;
        private String domain;
        private String path;
        private Boolean secure;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getMaxAge() {
            return maxAge;
        }

        public void setMaxAge(Integer maxAge) {
            this.maxAge = maxAge;
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public Boolean getSecure() {
            return secure;
        }

        public void setSecure(Boolean secure) {
            this.secure = secure;
        }
    }
}
