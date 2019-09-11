package com.github.taccisum.shiro.web.autoconfigure.stateless.support.extractor;

import com.github.taccisum.shiro.web.autoconfigure.stateless.support.extractor.enums.TokenKeyEnum;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.AbstractList;

/**
 * @author xiangtch
 * @date 2019/9/11 13:03
 * <p> Email: xiangtiancheng@deepexi.com </p>
 */
public class AbstractTokenExtractor implements TokenExtractor {

    private TokenKeyEnum tokenKeyEnum;

    protected AbstractTokenExtractor(TokenKeyEnum tokenKeyEnum){
        this.tokenKeyEnum = tokenKeyEnum;
    }

    @Override
    public String getToken(HttpServletRequest request) {
        return getToken(request, tokenKeyEnum.getTokenKey());
    }

    private String getToken(HttpServletRequest req, String tokenKey) {
        String token = req.getHeader(tokenKey);
        if (StringUtils.isEmpty(token)) {
            token = req.getParameter(tokenKey);
        }
        return token;
    }
}
