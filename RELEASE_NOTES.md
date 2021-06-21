# Release Notes

## v2.1.0

- 默认提供 authn filter

## v2.2.0

- 支持多级 payload

## v2.1.0

- JWTManager支持使用传入的payload template解析jwt
- 引入`TokenExtractor`，支持自定义token获取方式
- 提供`OnlyParseJWTRealm`

## v2.0.1

- 修复stateless模式下客户端未传`Accept`请求头时会出现`NullPointException`的bug
- 修复`StatelessUserFilter`中出现异常时需要经过servlet处理再重定向到error页面的问题

