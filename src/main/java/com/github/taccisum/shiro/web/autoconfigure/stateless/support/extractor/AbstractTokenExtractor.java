package com.github.taccisum.shiro.web.autoconfigure.stateless.support.extractor;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xiangtch - xiangtiancheng@deepexi.com
 * @since 2019/9/12 15:02
 */
public class AbstractTokenExtractor implements TokenExtractor {

    private String tokenKey;

    protected AbstractTokenExtractor(String tokenKey){
        this.tokenKey = tokenKey;
    }

    @Override
    public String getToken(HttpServletRequest request) {
        return getToken(request, tokenKey);
    }

    private String getToken(HttpServletRequest req, String tokenKey) {
        String token = req.getHeader(tokenKey);
        if (StringUtils.isEmpty(token)) {
            token = req.getParameter(tokenKey);
        }
        return token;
    }
}
