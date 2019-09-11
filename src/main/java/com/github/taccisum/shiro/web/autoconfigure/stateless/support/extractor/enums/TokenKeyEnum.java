package com.github.taccisum.shiro.web.autoconfigure.stateless.support.extractor.enums;

/**
 * @author xiangtch
 * @date 2019/9/11 11:31
 * <p> Email: xiangtiancheng@deepexi.com </p>
 */
public enum TokenKeyEnum {

    TOKEN("token"),
    AUTHORIZATION("Authorization");

    private String tokenKey;

    TokenKeyEnum(String tokenKey){
        this.tokenKey = tokenKey;
    }

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }
}
