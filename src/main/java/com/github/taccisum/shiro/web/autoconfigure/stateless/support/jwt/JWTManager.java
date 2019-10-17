package com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.taccisum.shiro.web.Model;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt.exception.BuildPayloadException;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt.exception.ErrorFieldException;
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
    private Map<String, PayloadTemplate> payloadTemplates = new HashMap<>(8);

    private ObjectMapper objectMapper = new ObjectMapper();

    public JWTManager() {
        this(new DefaultJWTAlgorithmProvider());
    }

    public JWTManager(JWTAlgorithmProvider algorithm) {
        this.algorithm = algorithm;
    }

    public PayloadTemplate getPayloadTemplate(String issuer) {
        return payloadTemplates.get(issuer);
    }

    public void addPayloadTemplate(PayloadTemplate payloadTemplate) {
        payloadTemplates.put(payloadTemplate.getIssuer(), payloadTemplate);
    }

    public String create(String issuer, Payload payload) {
        return create(issuer, payload, DEFAULT_EXPIRES_MINUTES);
    }

    public <T> String create(String issuer, Payload<T> payload, int expiresMinutes) {
        PayloadTemplate payloadTemplate = payloadTemplates.get(issuer);
        if (payloadTemplate == null) {
            throw new NotExistPayloadTemplateException(issuer);
        }

        boolean entityTemplate = Objects.nonNull(payloadTemplate.getModel());

        // check payloadTemplate valid
        if (entityTemplate) {
            validateModelType(payloadTemplate, payload);
        } else {
            payload.forEach((k, v) -> {
                payloadTemplate.check().hasField(k, v);
            });
            payloadTemplate.check().missingFields(payload);
        }

        JWTCreator.Builder builder = JWT.create()
                .withIssuer(issuer)
                .withExpiresAt(calculateExpiresTime(expiresMinutes));

        if (entityTemplate) {
            Model model = payload.getModel();
            try {
                builder.withClaim(model.getClaimName(), objectMapper.writeValueAsString(model.getEntity()));
            } catch (JsonProcessingException e) {
                // builder.withClaim(model.getClaimName(), model.getEntity().toString());
                throw new BuildPayloadException(String.format("error serialize model-entity: %s .", e.getMessage()));
            }
        } else {
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
        }

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

    public Payload parsePayload(String jwt, PayloadTemplate payloadTemplate) {
        return parsePayload(JWT.decode(jwt), payloadTemplate);
    }

    public <T> Payload parsePayload(DecodedJWT decodedJWT, PayloadTemplate<T> payloadTemplate) {
        if (payloadTemplate == null) {
            throw new NotExistPayloadTemplateException(decodedJWT.getIssuer());
        }

        Payload payload = new Payload();
        boolean entityTemplate = Objects.nonNull(payloadTemplate.getModel());
        if (entityTemplate) {
            Model<T> modelTemplate = payloadTemplate.getModel();
            Claim claim = decodedJWT.getClaim(modelTemplate.getClaimName());
            if (Objects.isNull(claim)) {
                throw new ParsePayloadException(String.format("there is not field %s on payload. check if you JWT is obsoleted.", modelTemplate.getClaimName()));
            }

            T entity;
            try {
                entity = objectMapper.readValue(claim.asString(), modelTemplate.getEntityType());
            } catch (Exception e) {
                throw new ParsePayloadException(e.getMessage());
            }

            payload.setModel(new Model(entity, modelTemplate.getClaimName()));
        } else {
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
        }

        return payload;
    }

    public Payload verifyAndParsePayload(String issuer, String jwt) throws JWTVerificationException {
        return parsePayload(verify(issuer, jwt));
    }

    static Date calculateExpiresTime(int expiresMinutes) {
        return new Date(System.currentTimeMillis() + expiresMinutes * 60 * 1000);
    }

    private JWTVerifier getVerifier(String issuer) {
        return JWT.require(algorithm.get())
                .withIssuer(issuer)
                .build();
    }

    protected static String newJWTId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private void validateModelType(PayloadTemplate payloadTemplate, Payload payload) {
        Model templateModel = payloadTemplate.getModel();
        Model model = payload.getModel();

        if (!Objects.equals(model.getEntity().getClass(), templateModel.getEntityType())) {
            throw new NotExistPayloadTemplateException("claims-entity should equals the-issuer-entityType");
        }
    }
}
