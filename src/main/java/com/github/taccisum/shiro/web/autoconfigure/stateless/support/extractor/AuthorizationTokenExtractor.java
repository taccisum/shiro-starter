package com.github.taccisum.shiro.web.autoconfigure.stateless.support.extractor;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xiangtch
 * @date 2019/9/11 10:14
 * <p> Email: xiangtiancheng@deepexi.com </p>
 */
public class AuthorizationTokenExtractor implements TokenExtractor{
    @Override
    public String getToken(HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        if (StringUtils.isEmpty(token)) {
            token = req.getParameter("Authorization");
        }

        return token.replaceAll("^Bearer\\s+", "");
    }
}
