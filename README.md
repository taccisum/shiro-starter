[![MIT Licence](https://badges.frapsoft.com/os/mit/mit.svg?v=103)](https://opensource.org/licenses/mit-license.php)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.taccisum/shiro-starter.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.taccisum%22%20AND%20a:%22shiro-starter%22)

[![Build Status](https://www.travis-ci.org/taccisum/shiro-starter.svg?branch=master)](https://www.travis-ci.org/taccisum/shiro-starter)
[![codecov](https://codecov.io/gh/taccisum/shiro-starter/branch/master/graph/badge.svg)](https://codecov.io/gh/taccisum/shiro-starter)

# 简介

由于官方的[`shiro-spring-boot-web-starter`](https://shiro.apache.org/spring-boot.html)提供的功能过于简单，因此这里又造了一个加强版的轮子。

[1.x版本链接](https://github.com/taccisum/tac-shiro-spring-boot-starter)

## 为什么选择shiro-starter

对比官方的starter，shiro-starter：
 - 是在官方starter的基础上扩展的，兼容官方starter
 - 将更多的参数配置化，使用起来更方便、灵活
 - 提供两种运行模式
   - `SESSION模式`[默认]：与Shiro官方提供的starter无二
   - `STATELESS模式`：无状态模式，也是现在许多大型系统喜欢采用的认证模式
 - 提供了一些常见的认证方案支撑（如JWT），通过简单配置即可集成


# 版本信息

 - spring-boot: `1.5.9.RELEASE`
 - shiro-spring: `1.4.0-RC2`
 
[RELEASE NOTES](/RELEASE_NOTES.md)

## 关于Spring Boot 2.x

该starter没有刻意针对`Spring Boot 2.x`做兼容，不过就实际使用反馈来说，在2.x环境下也暂时并没有出现什么兼容性问题，因此2.x用户也可以放心使用。
 

# 如何使用

## 引入依赖

```xml
<dependency>
  <groupId>com.github.taccisum</groupId>
  <artifactId>shiro-starter</artifactId>
  <version>{version}</version>
</dependency>
```

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

注意：在`Stateless`模式下，shiro认证是在filter中进行的，因此其中抛出的异常不会被spring MVC的异常处理器捕获，而是会重定向到error页面。

### 最小配置
1. 配置application.yml
```yaml
shiro:
  web:
    mode: stateless
    filter-chain-definition:
      anon:
        - /login
      authn:
        - /goods/recommendations
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
    // 支持自定义对象,List,Map等类型
    payload.put("map", map);
    payload.put("list", list);
    // 自定义对象
    payload.put("entity", entity);
    return jwtManager.create("access_token", payload);
}
```

2. 解析token为payload
```java
@Autowired
private JWTManager jwtManager;

public Payload parseJWT() {
    return jwtManager.verifyAndParsePayload("access_token", SecurityUtils.getSubject().getPrincipal().toString());
}
```

#### 其它特性
1. 指定payload格式

可以通过`PayloadTemplate`自由指定JWT payload的格式。

`JWTManager`在创建JWT时会根据JWT的`issuer`选取对应的`PayloadTemplate`校验payload是否合法。同时，在将JWT解析成payload时也会根据`PayloadTemplate`确定有哪些字段。

```java
@Bean
public PayloadTemplate payloadTemplate() {
    PayloadTemplate payloadTemplate = new DefaultPayloadTemplate("access_token");
    payloadTemplate.addField("uid", Long.class);
    payloadTemplate.addField("uname", String.class);
    payloadTemplate.addField("isAdmin", Boolean.class);
    payloadTemplate.addField("type", String.class);
    payloadTemplate.addField("roles", String.class);
    payloadTemplate.addField("list", List.class);
    payloadTemplate.addField("map", Map.class);
    payloadTemplate.addField("entity", Entity.class); // Entity为自定义类型
    return payloadTemplate;
}
```

`PayloadTemplate`可以创建多个，但每一个的`issuer`必须不同。当没有定义任何template时，会提供一个默认的template[issuer: access_token]：uid, username, roles, permissions。

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
 - 默认的`JWTAlgorithmProvider`将在每次应用启动时生成一个UUID作为密钥，因此在应用重启后，此前生成的JWT会全部失效


## 高级特性

### 自由配置shiro filters
通过覆写`ShiroFilterDefinition` bean即可实现
```java
    @Bean
    public ShiroFilterDefinition shiroFilterDefinition() {
        return filters -> {
            logger.info("replace [authc] filter by " + StatelessUserFilter.class);
            filters.put("authc", new StatelessUserFilter(shiroWebProperties));
            // add more filters
        };
    }
```


#### 额外提供的 filters

- authn: 仅当用户请求提供了 token 时才执行认证逻辑，未提供时将直接放行（视为匿名用户），适用于一些认证前后均可访问，且业务逻辑会有所不同的 api（例如商品推荐）

### 提供 OnlyParseJWTRealm 支持

这部分的封装是针对微服务架构中，由于系统间的调用往往是默认可信的，所以省去了 token 的验证过程，直接解析其中的 payload 供业务代码使用

指定 Realm

```java
@Bean
public Realm realm(String issuer, JWTManager jwtManager) {
    OnlyParseJWTRealm realm = new OnlyParseJWTRealm(issuer, jwtManager);
    realm.setCredentialsMatcher(new StatelessCredentialsMatcher());
    return realm;
}
```

### 支持自定义 token 获取方式

你可以通过提供 `TokenExtractor` 的 bean 来修改从 http 请求中获取 token 的逻辑，`shiro-starter` 默认提供了 `PowerTkExtractor` 供使用，如下

```java
@Bean
public TokenExtractor tokenExtractor(){
    PowerTkExtractor bean = new PowerTkExtractor("token", "tk", "auth");
    if (mock) {
        bean.setMock(true);
        bean.setMockTk("mock_tk");
    }
    return bean;
}
```

具体用法请参数 `PowerTkExtractor` 的 Javadoc。


## 配置一览

|properties|描述|默认值|适用模式|
|:--|:-|:-|:-|
|shiro.web.mode|指定shiro启动模式|SESSION|ALL|
|shiro.web.redirect-enabled|是否允许shiro重定向页面|true|ALL|
|shiro.web.filter-chain-definition|定义shiro filter chain|{}|ALL|
|shiro.loginUrl|用户未认证时重定向的页面|/login.jsp|ALL|
|shiro.successUrl|用户认证成功后跳转的默认页面|/|ALL|
|shiro.unauthorizedUrl|用户未授权时的重定向页面(403)|null|ALL|
|shiro.sessionManager.deleteInvalidSessions|移除无效session|true|SESSION|
|shiro.sessionManager.sessionIdCookieEnabled|启用cookie session ID|true|SESSION|
|shiro.sessionManager.sessionIdUrlRewritingEnabled|启用session URL重写支持|true|SESSION|
|shiro.userNativeSessionManager|使用原生的session manager|false|SESSION|
|shiro.sessionManager.cookie.name|session cookie名称|JSESSIONID|SESSION|
|shiro.sessionManager.cookie.maxAge|session cookie最大生存时间|-1|SESSION|
|shiro.sessionManager.cookie.domain|session cookie domain|null|SESSION|
|shiro.sessionManager.cookie.path|session cookie path|null|SESSION|
|shiro.sessionManager.cookie.secure|session cookie secure flag|false|SESSION|
|shiro.rememberMeManager.cookie.name|RememberMe cookie名称|JSESSIONID|SESSION|
|shiro.rememberMeManager.cookie.maxAge|RememberMe cookie最大生存时间|-1|SESSION|
|shiro.rememberMeManager.cookie.domain|RememberMe cookie domain|null|SESSION|
|shiro.rememberMeManager.cookie.path|RememberMe cookie path|null|SESSION|
|shiro.rememberMeManager.cookie.secure|RememberMe cookie secure flag|false|SESSION|


## 下一步计划
 - 支持OAuth2.0集成
 - 提供基于Redis存储的JWT Realm


