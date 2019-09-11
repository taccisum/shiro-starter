package com.github.taccisum.shiro.web.autoconfigure.stateless.support.extractor;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xiangtch
 * @date 2019/9/10 21:11
 * <p> Email: xiangtiancheng@deepexi.com </p>
 */
public interface TokenExtractor {

    String getToken(HttpServletRequest request);
}
