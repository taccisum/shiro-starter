package com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.taccisum.shiro.web.Model;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt.exception.ErrorFieldException;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt.exception.NotExistPayloadTemplateException;
import com.github.taccisum.shiro.web.autoconfigure.stateless.support.jwt.exception.ParsePayloadException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created By @author Zhouyuhang on 2019/10/15
 * <p></p>
 **/
public class JWTCustomManager extends JWTManager {

    private Map<String, Model> payloadTemplates = new HashMap<>(8);

    private ObjectMapper objectMapper = new ObjectMapper();

    public <T> void addClaimsTemplate(String issuer, Class<T> clazz, String claimsName) {
        payloadTemplates.put(issuer, new Model<>(clazz, claimsName));
    }

    public JWTCustomManager() {
        super();
    }

    public JWTCustomManager(JWTAlgorithmProvider algorithm) {
        super(algorithm);
    }

    public <T> String create(String issuer, T entity) {
        return this.create(issuer, entity, DEFAULT_EXPIRES_MINUTES);
    }

    public <T> String create(String issuer, T entity, int expiresMinutes) {
        Model payloadTemplate = payloadTemplates.get(issuer);

        if (Objects.isNull(payloadTemplate)) {
            throw new NotExistPayloadTemplateException(issuer);
        }

        if (Objects.isNull(entity)) {
            throw new ErrorFieldException("claims-entity can not null");
        }

        if (!Objects.equals(entity.getClass(), payloadTemplate.getModel())) {
            throw new NotExistPayloadTemplateException("claims-entity should equals the-issuer-payloadType");
        }

        String claimsJson;
        try {
            claimsJson = objectMapper.writeValueAsString(entity);
        } catch (JsonProcessingException e) {
            throw new ErrorFieldException(e.getMessage());
        }

        JWTCreator.Builder builder = JWT.create()
                .withIssuer(issuer)
                .withExpiresAt(calculateExpiresTime(expiresMinutes))
                .withClaim(payloadTemplate.getClaimsName(), claimsJson);

        return builder
                .withJWTId(newJWTId())
                .sign(super.algorithm.get());
    }

    public <T> T parseClaim(String jwt) {
        return this.parseClaim(JWT.decode(jwt));
    }

    public <T> T parseClaim(DecodedJWT decodedJWT) {
        if (Objects.isNull(decodedJWT)) {
            throw new NullPointerException("DecodedJWT can not NULL");
        }

        Model<T> model = payloadTemplates.get(decodedJWT.getIssuer());
        if (Objects.isNull(model)) {
            throw new NotExistPayloadTemplateException(decodedJWT.getIssuer());
        }

        return doParseClaim(decodedJWT, model);
    }

    private <T> T doParseClaim(DecodedJWT decodedJWT, Model<T> model) {
        String claimsName = model.getClaimsName();
        Claim claim = decodedJWT.getClaim(claimsName);
        if (Objects.isNull(claim)) {
            throw new ParsePayloadException(String.format("there is not field %s on payload. check if you JWT is obsoleted.", claimsName));
        }

        T t;
        try {
            t = objectMapper.readValue(claim.asString(), model.getModel());
        } catch (Exception e) {
            throw new ParsePayloadException(e.getMessage());
        }
        return t;
    }
}

