package com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/5
 */
public class Payload extends HashMap<String, Object> {
    @Override
    public Object put(String key, Object value) {
        return super.put(key, value);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        super.putAll(m);
    }
}
