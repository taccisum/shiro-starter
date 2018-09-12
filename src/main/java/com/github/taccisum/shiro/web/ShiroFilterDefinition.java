package com.github.taccisum.shiro.web;

import javax.servlet.Filter;
import java.util.Map;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/12
 */
public interface ShiroFilterDefinition {
    void define(Map<String, Filter> filters);
}
