package com.github.taccisum.shiro.web.autoconfigure.stateless.support.extractor;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xiangtch
 * @date 2019/9/12 15:06
 * <p> Email: xiangtiancheng@deepexi.com </p>
 */
public class AuthorizationTokenExtractor extends AbstractTokenExtractor {

    public AuthorizationTokenExtractor() {
        super("Authorization");
    }

    @Override
    public String getToken(HttpServletRequest request) {
        return super.getToken(request).replace("^Bearer\\s+", "");
    }
}
