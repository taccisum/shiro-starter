package com.github.taccisum.shiro.web.autoconfigure.stateless.integration;

import com.github.taccisum.shiro.web.ShiroFilterDefinition;
import com.github.taccisum.shiro.web.ShiroWebProperties;
import com.github.taccisum.shiro.web.autoconfigure.stateless.ShiroWebFilterAutoConfiguration;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.StatelessCredentialsMatcher;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.StatelessUserFilter;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.extractor.AuthorizationTokenExtractor;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.extractor.TokenExtractor;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.hash.SimpleHashRealm;
import org.apache.shiro.realm.Realm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/3
 */
@SpringBootApplication
public class ShiroStatelessModeApplication {

    private Logger logger = LoggerFactory.getLogger(ShiroStatelessModeApplication.class);

    @Autowired
    private ShiroWebProperties shiroWebProperties;

    @Bean
    public Realm realm() {
        SimpleHashRealm realm = new SimpleHashRealm();
        realm.setCredentialsMatcher(new StatelessCredentialsMatcher());
        realm.addAccount("cd6765734a16476b9bd4b0513b3fb8e4", "staff");
        return realm;
    }

}
