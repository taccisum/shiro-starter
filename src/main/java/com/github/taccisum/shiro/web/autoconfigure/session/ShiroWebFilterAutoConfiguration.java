package com.github.taccisum.shiro.web.autoconfigure.session;

import com.github.taccisum.shiro.web.autoconfigure.AbstractShiroWebFilterAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/3
 */
@Configuration
@ConditionalOnProperty(name = "shiro.web.mode", havingValue = "SESSION", matchIfMissing = true)
public class ShiroWebFilterAutoConfiguration extends AbstractShiroWebFilterAutoConfiguration {
}
