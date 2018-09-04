package com.github.taccisum.shiro.web.autoconfigure;

import com.github.taccisum.shiro.web.ShiroWebProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/4
 */
@Configuration
@EnableConfigurationProperties(ShiroWebProperties.class)
public class ShiroWebAutoConfiguration {
}
