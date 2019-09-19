package com.github.taccisum.shiro.web.autoconfigure.stateless.support.extractor;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xiangtch - xiangtiancheng@deepexi.com
 * @since 2019/9/12 14:59
 */
public interface TokenExtractor {
    /**
     *  从请求中获取 token 信息
     *
     * @param req 请求
     * @return token
     */
    String getToken(HttpServletRequest req);
}
