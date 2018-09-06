[![Build Status](https://www.travis-ci.org/taccisum/shiro-starter.svg?branch=master)](https://www.travis-ci.org/taccisum/shiro-starter)


# 简介

由于官方的[`shiro-spring-boot-web-starter`](https://shiro.apache.org/spring-boot.html)提供的功能过于简单，因此这里又造了一个加强版的轮子。

## 为什么选择shiro-starter

对比官方的starter，shiro-starter：
 - 是在官方starter的基础上扩展的，兼容官方starter
 - 将更多的参数配置化，使用起来更方便、灵活
 - 提供两种运行模式
   - `Session模式`[默认]：与Shiro官方提供的starter无二
   - `Stateless模式`：无状态模式，也是现在许多大型系统喜欢采用的认证模式
 - 提供了一些常见的认证方案支撑（如JWT），通过简单配置即可集成


# 版本信息

 - spring-boot: `1.5.9.RELEASE`
 - shiro-spring: `1.4.0-RC2`


# 如何使用

## Session模式

### 最小配置
1. 配置application.yml
```yaml
shiro:
    filter-chain-definition:
      anon:
        - /login
        - /logout
      authc:
        - /**
```

2. 配置realm

```java
@Bean
public Realm realm() {
    SimpleAccountRealm realm = new SimpleAccountRealm("test_realm");
    realm.addAccount("tac", "123456", "staff");
    return realm;
}
```

## Stateless模式

### 最小配置
1. 配置application.yml
```yaml
shiro:
  web:
    mode: stateless
    filter-chain-definition:
      anon:
        - /login
      authc:
        - /**
```

2. 指定realm
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

### 使用JWT作为token

#### 最小配置
1. 添加JWT依赖
```xml
<dependency>
    <groupId>com.auth0</groupId>
    <artifactId>java-jwt</artifactId>
    <version>3.4.0</version>
</dependency>
```

shiro-starter检测到存在该依赖时会自动创建`JWTManager`及相关bean供realm使用。

2. 指定realm
```java
@Bean
public Realm realm(JWTManager jwtManager) {
    SimpleJWTRealm realm = new SimpleJWTRealm(jwtManager);
    realm.setCredentialsMatcher(new StatelessCredentialsMatcher());
    return realm;
}
```

然后你可以：

1. 创建JWT
```java
@Autowired
private JWTManager jwtManager;

public String login() {
    Payload payload = new Payload();
    payload.put("uid", 123L);
    payload.put("username", "tac");
    payload.put("roles", "STAFF,DEVELOPER");
    payload.put("permissions", "system:user:view,system:user:add");
    return jwtManager.create(payload);
}
```

2. 解析token为payload
```java
@Autowired
private JWTManager jwtManager;

public Payload parseJWT() {
    return jwtManager.verifyAndParsePayload(SecurityUtils.getSubject().getPrincipal().toString());
}
```

#### 其它特性
1. 指定payload格式

可以通过`PayloadTemplate`自由指定JWT payload的格式。默认为：uid, username, roles, permissions。

`JWTManager`在创建JWT时会根据`PayloadTemplate`校验payload是否合法。同时，在将JWT解析成payload时也会根据`PayloadTemplate`确定有哪些字段。

```java
@Bean
public PayloadTemplate payloadTemplate() {
    PayloadTemplate payloadTemplate = new PayloadTemplate();
    payloadTemplate.addField("uid", Long.class);
    payloadTemplate.addField("uname", String.class);
    payloadTemplate.addField("isAdmin", Boolean.class);
    payloadTemplate.addField("type", String.class);
    payloadTemplate.addField("roles", String.class);
    return payloadTemplate;
}
```

2. 指定加密算法

可以通过`JWTAlgorithmProvider`指定JWT的加密算法。默认为`HMAC256`。

```java
@Bean
protected JWTAlgorithmProvider jwtAlgorithmProvider() {
    return new DefaultJWTAlgorithmProvider();
}
```

#### 注意事项
 - `SimpleJWTRealm`不支持登出操作，每个JWT都有固定的有效时间，无法强制使其失效

## 其它问题

### Stateless模式下的认证异常处理



