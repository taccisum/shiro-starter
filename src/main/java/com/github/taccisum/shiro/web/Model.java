package com.github.taccisum.shiro.web;

import com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt.exception.MissingFieldsException;

import java.util.Objects;

/**
 * Created By @author Zhouyuhang on 2019/10/15
 * <p></p>
 **/
public class Model<T> {

    private Class<T> model;

    private String claimsName;

    public Model(Class<T> clazz, String claimsName) {
        if (Objects.isNull(clazz) || Objects.isNull(claimsName)) {
            throw new MissingFieldsException("Create Model Error");
        }
        this.model = clazz;
        this.claimsName = claimsName;
    }

    public Class<T> getModel() {
        return model;
    }

    public String getClaimsName() {
        return claimsName;
    }
}
