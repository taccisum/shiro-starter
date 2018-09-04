[![Build Status](https://www.travis-ci.org/taccisum/shiro-starter.svg?branch=master)](https://www.travis-ci.org/taccisum/shiro-starter)


# 简介

由于官方的[`shiro-spring-boot-web-starter`](https://shiro.apache.org/spring-boot.html)提供的功能过于简单，因此这里又造了一个加强版的轮子。


# 版本信息

shiro-spring: `1.4.0-RC2`


# 如何使用

## Session模式最小配置

### realm
```java
@Bean
public Realm realm() {
    SimpleAccountRealm realm = new SimpleAccountRealm("test_realm");
    realm.addAccount("tac", "123456", "staff");
    return realm;
}
```

## Stateless模式最小配置

### application.yml
```yaml
shiro:
  web:
    mode: stateless
```

### realm
```java
@Bean
public Realm realm() {
    SimpleHashRealm realm = new SimpleHashRealm();
    realm.setCredentialsMatcher(new StatelessCredentialsMatcher());     //这行不能少，否则在token模式下凭证校验会不通过
    realm.addAccount("cd6765734a16476b9bd4b0513b3fb8e4", "staff");
    return realm;
}
```

然后通过`header`或`parameter`传入token={hash}，`StatelessUserFilter`会自动调用`subject.login(token)`为每一次无状态请求进行认证。


## 配置shiro filter chain

### application.yml
```yaml
shiro:
  web:
    filter-chain-definition:
      anon:
        - /login
        - /logout
      authc:
        - /**
```

**注意事项**
 - 配置顺序会影响过滤器执行顺序。


## 其它问题

### Stateless模式下的认证异常处理



