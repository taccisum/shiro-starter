package com.github.taccisum.shiro.web.autoconfigure.stateless.support;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/4
 */
public class StatelessUserFilter extends UserFilter {
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (this.isLoginRequest(request, response)) {
            return true;
        } else {
            HttpServletRequest req = (HttpServletRequest) request;
            String token = getToken(req);
            if (StringUtils.isEmpty(token)) {
                return false;
            }
            SecurityUtils.getSubject().login(new StatelessToken(token));
            return true;
        }
    }

    private String getToken(HttpServletRequest req) {
        String token = req.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            token = req.getParameter("token");
        }
        return token;
    }

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (isAjax(request)) {
            // disable redirect
            // todo::
            PrintWriter writer = response.getWriter();
            writer.write("unauthenticated user");
            return false;
        }
        this.redirectToLogin(request, response);
        return false;
    }

    private boolean isAjax(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        return Objects.equals(req.getHeader("Accept"), "application/json");
    }
}
