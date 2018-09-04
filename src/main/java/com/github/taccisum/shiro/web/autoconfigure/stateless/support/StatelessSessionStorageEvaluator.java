package com.github.taccisum.shiro.web.autoconfigure.stateless.support;

import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/4
 */
public class StatelessSessionStorageEvaluator extends DefaultSessionStorageEvaluator {
    public StatelessSessionStorageEvaluator() {
        super();
        setSessionStorageEnabled(false);
    }
}
