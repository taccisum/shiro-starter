package com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt;

import com.github.taccisum.shiro.web.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/5
 */
public class Payload<T> extends HashMap<String, Object> {

    private Model<T> model;

    public Model<T> getModel() {
        return model;
    }

    public Payload setModel(Model model) {
        this.model = model;
        return this;
    }

    public Payload() {
        super();
    }

    @Override
    public Object put(String key, Object value) {
        return super.put(key, value);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        super.putAll(m);
    }
}
