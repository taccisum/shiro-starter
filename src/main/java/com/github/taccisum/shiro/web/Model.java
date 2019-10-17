package com.github.taccisum.shiro.web;

import com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt.exception.MissingFieldsException;

import java.util.Objects;

/**
 * Created By @author Zhouyuhang on 2019/10/15
 * <p></p>
 **/
public class Model<T> {

    private Class<T> entityType;

    private T entity;

    private String claimName;

    public Model(Class<T> entityType, String claimName) {
        this(claimName);
        if (Objects.isNull(entityType)) {
            throw new MissingFieldsException("Create Model Error");
        }
        this.entityType = entityType;
    }

    public Model(T entity, String claimName) {
        this(claimName);
        if (Objects.isNull(entity)) {
            throw new MissingFieldsException("Create Model Error");
        }
        this.entity = entity;
    }

    private Model(String claimName) {
        if (claimName.isEmpty()) {
            throw new MissingFieldsException("Create Model Error");
        }
        this.claimName = claimName;
    }

    public Class<T> getEntityType() {
        return entityType;
    }

    public String getClaimName() {
        return claimName;
    }

    public T getEntity() {
        return entity;
    }

//    public void setClaimName(String claimName) {
//        this.claimName = claimName;
//    }
}