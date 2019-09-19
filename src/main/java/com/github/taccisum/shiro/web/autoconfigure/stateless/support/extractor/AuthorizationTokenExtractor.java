package com.github.taccisum.shiro.web.autoconfigure.stateless.support.extractor;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xiangtch - xiangtiancheng@deepexi.com
 * @since 2019/9/12 15:06
 */
public class AuthorizationTokenExtractor extends AbstractTokenExtractor {

    public AuthorizationTokenExtractor() {
        super("Authorization");
    }

    @Override
    public String getToken(HttpServletRequest request) {
        return super.getToken(request).replaceAll("^Bearer\\s+", "");
    }
}
