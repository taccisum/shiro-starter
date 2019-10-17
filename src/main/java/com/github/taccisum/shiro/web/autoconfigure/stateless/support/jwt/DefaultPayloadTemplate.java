package com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt;

import com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt.exception.BuildPayloadException;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt.exception.ErrorFieldException;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt.exception.MissingFieldsException;

import java.util.*;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/12
 */
public class DefaultPayloadTemplate implements PayloadTemplate {
    private String issuer;
    private Map<String, Class> fieldMap = new LinkedHashMap<>();
    private PayloadChecker checker = new DefaultPayloadChecker(this);

    public DefaultPayloadTemplate(String issuer) {
        this.issuer = issuer;
    }

    public String getIssuer() {
        return issuer;
    }

    public void addField(String key, Class type) {
        fieldMap.put(key, type);
    }

    public boolean hasField(String key, Class type) {
        return Objects.equals(fieldMap.get(key), type);
    }

    public boolean hasFieldForCollection(String key, Object entity) {
        Class templateClazzType = fieldMap.get(key);
        if ((Objects.equals(templateClazzType, List.class) && entity instanceof List)
                || (Objects.equals(templateClazzType, Map.class) && entity instanceof Map)
                || (Objects.equals(templateClazzType, Set.class) && entity instanceof Set)) {
            return true;
        }
        return false;
    }

    public Set<String> getFieldNames() {
        return fieldMap.keySet();
    }

    public Map<String, Class> getFieldMap() {
        return fieldMap;
    }

    public List<String> extractMissingFields(Payload payload) {
        List<String> missingFieldNames = new ArrayList<>();
        for (String fieldName : getFieldNames()) {
            if (payload.get(fieldName) == null) {
                missingFieldNames.add(fieldName);
            }
        }
        return missingFieldNames;
    }

    public PayloadChecker check() {
        return checker;
    }

    public static class DefaultPayloadChecker implements PayloadChecker {
        private PayloadTemplate template;

        public DefaultPayloadChecker(PayloadTemplate template) {
            this.template = template;
        }

        public void hasField(String key, Object value) throws ErrorFieldException {
            if (Objects.isNull(value) || Objects.isNull(key)) {
                throw new BuildPayloadException("payload error:key and value can not NULL");
            }

            boolean isCollection = (value instanceof Collection || value instanceof Map);
            boolean hasField;
            if (isCollection) {
                hasField = template.hasFieldForCollection(key, value);
            } else {
                hasField = template.hasField(key, value.getClass());
            }

            if (!hasField) {
                throw new ErrorFieldException(String.format("template does not has field: %s[%s]. you can not put it into payload.", key, value.getClass()));
            }
        }

        /**
         * @throws MissingFieldsException when missing fields' size grater than 0
         */
        public void missingFields(Payload payload) throws MissingFieldsException {
            List<String> missingFields = template.extractMissingFields(payload);
            if (missingFields.size() > 0) {
                throw new MissingFieldsException(String.format("build failure. missing field: %s. please implement these before execute build.", missingFields));
            }
        }
    }
}
