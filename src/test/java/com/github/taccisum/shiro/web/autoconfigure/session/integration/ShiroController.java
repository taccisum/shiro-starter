package com.github.taccisum.shiro.web.autoconfigure.session.integration;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/3
 */
@RestController
public class ShiroController {
    private Logger logger = LoggerFactory.getLogger(ShiroController.class);

    @GetMapping("login")
    public Boolean login() {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            subject.login(new UsernamePasswordToken("tac", "123456"));
            logger.info("hello " + subject.getPrincipal());
            return true;
        }
        return false;
    }

    @GetMapping("logout")
    public boolean logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            subject.logout();
            logger.info("logout successfully");
            return true;
        }
        return false;
    }

    @GetMapping("require_staff")
    @RequiresRoles("staff")
    public String staff() {
        return "staff";
    }

    @GetMapping("require_user")
    @RequiresRoles("user")
    public String user() {
        return "user";
    }
}
