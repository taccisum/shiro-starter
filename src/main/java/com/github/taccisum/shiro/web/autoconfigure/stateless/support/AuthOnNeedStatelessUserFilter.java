package com.github.taccisum.shiro.web.autoconfigure.stateless.support;

import com.github.taccisum.shiro.web.ShiroWebProperties;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.extractor.TokenExtractor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * @author taccisum - liaojinfeng6938@dingtalk.com
 * @since 2021/6/21
 */
public class AuthOnNeedStatelessUserFilter extends StatelessUserFilter {
    public AuthOnNeedStatelessUserFilter(ShiroWebProperties shiroWebProperties, TokenExtractor tokenExtractor) {
        super(shiroWebProperties, tokenExtractor);
    }

    @Override
    protected boolean login(ServletRequest request) {
        HttpServletRequest req = WebUtils.toHttp(request);
        String token = tokenExtractor.getToken(req);
        if (token == null || token.trim().length() == 0) {
            // 当 token 为 empty 时表示不需要登录，直接放行，由业务代码自行处理认证 & 未认证两种不同的情况
            return true;
        }
        SecurityUtils.getSubject().login(new StatelessToken(token));
        return true;
    }
}
