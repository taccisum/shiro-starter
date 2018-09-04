package com.github.taccisum.shiro.web.autoconfigure.session.integration;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/3
 */
@SpringBootApplication
public class ShrioSessionModeApplication {
    @Bean
    public Realm realm() {
        SimpleAccountRealm realm = new SimpleAccountRealm("test_realm");
        realm.addAccount("tac", "123456", "staff");
        return realm;
    }
}
