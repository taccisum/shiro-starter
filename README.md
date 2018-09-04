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

### shiroFilterChainDefinition
```java
@Bean
public ShiroFilterChainDefinition shiroFilterChainDefinition() {
    DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
    chainDefinition.addPathDefinition("/login", "anon");
    chainDefinition.addPathDefinition("/logout", "anon");
    chainDefinition.addPathDefinition("/**", "authc");
    return chainDefinition;
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
    realm.setCredentialsMatcher(new StatelessCredentialsMatcher());
    realm.addAccount("cd6765734a16476b9bd4b0513b3fb8e4", "staff");
    return realm;
}
```

### shiroFilterChainDefinition
```java
@Bean
public ShiroFilterChainDefinition shiroFilterChainDefinition() {
    DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
    chainDefinition.addPathDefinition("/login", "anon");
    chainDefinition.addPathDefinition("/**", "authc");
    return chainDefinition;
}
```

然后通过`header`或`parameter`传入token={hash}，`StatelessUserFilter`会自动调用`subject.login(token)`为每一次无状态请求进行认证。



