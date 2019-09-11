package com.github.taccisum.shiro.web.autoconfigure.stateless.support.extractor;

import com.github.taccisum.shiro.web.autoconfigure.stateless.support.extractor.enums.TokenKeyEnum;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xiangtch
 * @date 2019/9/11 10:11
 * <p> Email: xiangtiancheng@deepexi.com </p>
 */
public class DefaultTokenExtractor extends AbstractTokenExtractor {

    public DefaultTokenExtractor() {
        super(TokenKeyEnum.TOKEN);
    }
}
