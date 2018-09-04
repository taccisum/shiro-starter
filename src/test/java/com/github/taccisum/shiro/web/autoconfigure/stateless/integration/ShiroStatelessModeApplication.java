package com.github.taccisum.shiro.web.autoconfigure.stateless.integration;

import com.github.taccisum.shiro.web.autoconfigure.stateless.support.StatelessCredentialsMatcher;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.StatelessToken;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/3
 */
@SpringBootApplication
public class ShiroStatelessModeApplication {
    private static Map<String, SimpleAccount> map = new HashMap<>();

    public static void addAccount(String realmName, String hash, String... roles) {
        map.put(hash, new SimpleAccount(hash, null, realmName, CollectionUtils.asSet(roles), null));
    }

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

    public static class SimpleHashRealm extends AuthorizingRealm {
        @Override
        public String getName() {
            return "hash_realm";
        }

        @Override
        public boolean supports(AuthenticationToken token) {
            return token instanceof StatelessToken;
        }

        public void addAccount(String hash, String... roles) {
            ShiroStatelessModeApplication.addAccount(this.getName(), hash, roles);
        }

        @Override
        protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
            StatelessToken token = (StatelessToken) authenticationToken;
            SimpleAccount account = map.get(token.getPrincipal());
            if (account != null) {
                if (account.isLocked()) {
                    throw new LockedAccountException("Account [" + account + "] is locked.");
                }

                if (account.isCredentialsExpired()) {
                    String msg = "The credentials for account [" + account + "] are expired";
                    throw new ExpiredCredentialsException(msg);
                }
            }
            return account;
        }

        @Override
        protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
            SimpleAccount account = map.get(principalCollection.getPrimaryPrincipal());
            return account;
        }
    }
}
