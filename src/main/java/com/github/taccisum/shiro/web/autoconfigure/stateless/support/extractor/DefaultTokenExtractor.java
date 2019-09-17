package com.github.taccisum.shiro.web.autoconfigure.stateless.support.extractor;

import com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt.AbstractJWTRealm;

/**
 * @author xiangtch
 * @date 2019/9/12 15:05
 * <p> Email: xiangtiancheng@deepexi.com </p>
 */
public class DefaultTokenExtractor extends AbstractTokenExtractor {

    public DefaultTokenExtractor() {
        super("token");
    }
}
