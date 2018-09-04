package com.github.taccisum.shiro.web.autoconfigure.stateless.integration;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/4
 */
@RestController
public class ShiroController {
    private Logger logger = LoggerFactory.getLogger(ShiroController.class);

    @GetMapping("login")
    public String login() {
        return "cd6765734a16476b9bd4b0513b3fb8e4";
    }

//    因为是无状态的，所以logout功能不能简单地通过shiro实现
//    @GetMapping("logout")
//    public boolean logout() {
//        return true;
//    }

    @GetMapping("info")
    public Object info() {
        HashMap<String, Object> info = new HashMap<>();
        Object hash = SecurityUtils.getSubject().getPrincipal();
        info.put("username", hash);
        return info;
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
