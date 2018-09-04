package com.github.taccisum.shiro.web.autoconfigure.stateless.support;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/4
 */
public class StatelessSubjectFactory extends DefaultWebSubjectFactory {
    @Override
    public Subject createSubject(SubjectContext context) {
        context.setSessionCreationEnabled(false);
        return super.createSubject(context);
    }
}
