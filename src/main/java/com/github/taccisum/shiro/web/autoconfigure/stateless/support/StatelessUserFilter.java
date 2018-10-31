package com.github.taccisum.shiro.web.autoconfigure.stateless.support;

import com.github.taccisum.shiro.web.ShiroWebProperties;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/4
 */
public class StatelessUserFilter extends UserFilter {
    private ShiroWebProperties shiroWebProperties;

    public StatelessUserFilter(ShiroWebProperties shiroWebProperties) {
        this.shiroWebProperties = shiroWebProperties;
    }

    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (this.isLoginRequest(request, response)) {
            return true;
        } else {
            try {
                return login(request);
            } catch (Exception e) {
                if (request.getAttribute("javax.servlet.error.exception") == null) {
                    request.setAttribute("javax.servlet.error.exception", e);
                }
                return false;
            }
        }
    }

    protected boolean login(ServletRequest request) {
        HttpServletRequest req = WebUtils.toHttp(request);
        String token = getToken(req);
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        SecurityUtils.getSubject().login(new StatelessToken(token));
        return true;
    }

    private String getToken(HttpServletRequest req) {
        String token = req.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            token = req.getParameter("token");
        }
        return token;
    }

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (request.getAttribute("javax.servlet.error.exception") != null) {
            // some exception happen on isAccessAllowed()
            // handle onAccessDenied() by spring ErrorController via send error here
            WebUtils.toHttp(response).sendError(HttpStatus.UNAUTHORIZED.value(), "unauthenticated user");
        } else if (acceptHtml(request) && shiroWebProperties.getRedirectEnabled()) {
            this.redirectToLogin(request, response);
        } else {
            request.setAttribute("javax.servlet.error.exception", new UnauthenticatedException("unauthenticated user"));
            WebUtils.toHttp(response).sendError(HttpStatus.UNAUTHORIZED.value(), "unauthenticated user");
        }
        return false;
    }

    static boolean acceptHtml(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        String accept = req.getHeader("Accept");
        return accept != null && accept.contains("text/html");
    }
}
