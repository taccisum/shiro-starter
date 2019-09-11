package com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt.exception.NotExistPayloadTemplateException;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt.exception.ParsePayloadException;

import java.util.*;

/**
 * @author tac - liaojf@cheegu.com
 * @since 2018/9/4
 */
public class JWTManager {
    private static final int DEFAULT_EXPIRES_MINUTES = 60 * 24;

    private JWTAlgorithmProvider algorithm;
    private Map<String, PayloadTemplate> payloadTemplates = new HashMap<>();

    public JWTManager() {
        this(new DefaultJWTAlgorithmProvider());
    }

    public JWTManager(JWTAlgorithmProvider algorithm) {
        this.algorithm = algorithm;
    }

    public PayloadTemplate getPayloadTemplate(String key) {
        return payloadTemplates.get(key);
    }

    public void addPayloadTemplate(PayloadTemplate payloadTemplate) {
        payloadTemplates.put(payloadTemplate.getIssuer(), payloadTemplate);
    }


    public String create(String issuer, Payload payload) {
        return create(issuer, payload, DEFAULT_EXPIRES_MINUTES);
    }

    public String create(String issuer, Payload payload, int expiresMinutes) {
        PayloadTemplate payloadTemplate = payloadTemplates.get(issuer);
        if (payloadTemplate == null) {
            throw new NotExistPayloadTemplateException(issuer);
        }
        payload.forEach((k, v) -> {
            payloadTemplate.check().hasField(k, v);
        });
        payloadTemplate.check().missingFields(payload);

        JWTCreator.Builder builder = JWT.create()
                .withIssuer(issuer)
                .withExpiresAt(calculateExpiresTime(expiresMinutes));

        payload.forEach((k, v) -> {
            if (v instanceof Boolean) {
                builder.withClaim(k, (Boolean) v);
            } else if (v instanceof Integer) {
                builder.withClaim(k, (Integer) v);
            } else if (v instanceof Long) {
                builder.withClaim(k, (Long) v);
            } else if (v instanceof Double) {
                builder.withClaim(k, (Double) v);
            } else if (v instanceof Date) {
                builder.withClaim(k, (Date) v);
            } else if (v instanceof String) {
                builder.withClaim(k, (String) v);
            } else {
                builder.withClaim(k, v.toString());
            }
        });
        return builder
                .withJWTId(newJWTId())
                .sign(algorithm.get());
    }

    public DecodedJWT verify(String issuer, String jwt) throws JWTVerificationException {
        return getVerifier(issuer).verify(jwt);
    }

    public Payload parsePayload(String jwt) {
        return parsePayload(JWT.decode(jwt));
    }

    public Payload parsePayload(DecodedJWT decodedJWT) {
        PayloadTemplate payloadTemplate = payloadTemplates.get(decodedJWT.getIssuer());
        return parsePayload(decodedJWT, payloadTemplate);
    }

    public Payload parsePayload(DecodedJWT decodedJWT, PayloadTemplate payloadTemplate) {
        Payload payload = new Payload();
        if (payloadTemplate == null) {
            throw new NotExistPayloadTemplateException(decodedJWT.getIssuer());
        }
        payloadTemplate.getFieldMap().forEach((k, v) -> {

            Claim claim = decodedJWT.getClaim(k);
            if (claim.isNull()) {
                throw new ParsePayloadException(String.format("there is not field %s[%s] on payload. check if you JWT is obsoleted.", k, v));
            }
            if (Objects.equals(v, Boolean.class)) {
                payload.put(k, claim.asBoolean());
            } else if (Objects.equals(v, Integer.class)) {
                payload.put(k, claim.asInt());
            } else if (Objects.equals(v, Long.class)) {
                payload.put(k, claim.asLong());
            } else if (Objects.equals(v, Double.class)) {
                payload.put(k, claim.asDouble());
            } else if (Objects.equals(v, Date.class)) {
                payload.put(k, claim.asDate());
            } else if (Objects.equals(v, String.class)) {
                payload.put(k, claim.asString());
            } else {
                payload.put(k, claim.asString());
            }
        });
        return payload;
    }

    public Payload verifyAndParsePayload(String issuer, String jwt) throws JWTVerificationException {
        return parsePayload(verify(issuer, jwt));
    }

    static Date calculateExpiresTime(int expiresMinutes) {
        return new Date(new Date().getTime() + expiresMinutes * 60 * 1000);
    }

    private JWTVerifier getVerifier(String issuer) {
        return JWT.require(algorithm.get())
                .withIssuer(issuer)
                .build();
    }

    private static String newJWTId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
