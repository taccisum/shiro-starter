package com.github.taccisum.shiro.web.autoconfigure.stateless.integration;

import com.github.taccisum.shiro.web.autoconfigure.stateless.support.StatelessCredentialsMatcher;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.hash.SimpleHashRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/3
 */
@SpringBootApplication
public class ShiroStatelessModeApplication {
    @Bean
    public Realm realm() {
        SimpleHashRealm realm = new SimpleHashRealm();
        realm.setCredentialsMatcher(new StatelessCredentialsMatcher());
        realm.addAccount("cd6765734a16476b9bd4b0513b3fb8e4", "staff");
        return realm;
    }

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        chainDefinition.addPathDefinition("/login", "anon");
        chainDefinition.addPathDefinition("/**", "authc");
        return chainDefinition;
    }
}
