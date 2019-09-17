package com.github.taccisum.shiro.web.autoconfigure.stateless.support.extractor;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xiangtch
 * @date 2019/9/12 14:59
 * <p> Email: xiangtiancheng@deepexi.com </p>
 */
public interface TokenExtractor {
    /**
     *  从请求中获取 token 信息
     *
     * @param req
     * @return
     */
    String getToken(HttpServletRequest req);
}
