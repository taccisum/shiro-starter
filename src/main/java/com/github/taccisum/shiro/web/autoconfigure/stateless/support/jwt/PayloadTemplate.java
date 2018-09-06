package com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt;

import com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt.exception.ErrorFieldException;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt.exception.MissingFieldsException;

import java.util.*;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/6
 */
public class PayloadTemplate {
    private Map<String, Class> fieldMap = new LinkedHashMap<>();
    private PayloadChecker checker = new PayloadChecker(this);

    public void addField(String key, Class type) {
        fieldMap.put(key, type);
    }

    public boolean hasField(String key, Class type) {
        return Objects.equals(fieldMap.get(key), type);
    }

    public Set<String> getFieldNames() {
        return fieldMap.keySet();
    }

    public Map<String, Class> getFieldMap() {
        return fieldMap;
    }

    /**
     * @return missing fields name collection
     */
    public List<String> extractMissingFields(Payload payload) {
        List<String> missingFieldNames = new ArrayList<>();
        for (String fieldName : getFieldNames()) {
            if (payload.get(fieldName) == null) {
                missingFieldNames.add(fieldName);
            }
        }
        return missingFieldNames;
    }

//    public PayloadBuilder create() {
//        return new PayloadBuilder(this);
//    }

    public PayloadChecker check() {
        return checker;
    }

//    public static class PayloadBuilder {
//        private PayloadTemplate template;
//        private Payload payload = new Payload();
//
//        public PayloadBuilder(PayloadTemplate payloadTemplate) {
//            this.template = payloadTemplate;
//        }
//
//        public PayloadBuilder put(String key, Object value) {
//            template.check().hasField(key, value);
//            payload.put(key, value);
//            return this;
//        }
//
//        public Payload build() {
//            template.check().missingFields(payload);
//            return payload;
//        }
//    }

    public static class PayloadChecker {
        private PayloadTemplate template;

        public PayloadChecker(PayloadTemplate template) {
            this.template = template;
        }

        public void hasField(String key, Object value) {
            if (!template.hasField(key, value.getClass())) {
                throw new ErrorFieldException(String.format("template does not has field: %s[%s]. you can not put it into payload.", key, value.getClass()));
            }
        }

        /**
         * @throws MissingFieldsException when missing fields' size grater than 0
         */
        public void missingFields(Payload payload) {
            List<String> missingFields = template.extractMissingFields(payload);
            if (missingFields.size() > 0) {
                throw new MissingFieldsException(String.format("build failure. missing field: %s. please implement these before execute build.", missingFields));
            }
        }
    }
}
