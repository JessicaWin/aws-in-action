package com.jessica.cognito.service;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import java.util.Base64;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jessica.cognito.model.CognitoJWTClaim;
import com.jessica.cognito.model.CognitoJwkKey;
import com.jessica.cognito.model.CognitoJwksResponse;
import com.jessica.cognito.model.CognitoToken;
import com.jessica.cognito.property.BasePropertySource;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CognitoTokenService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CognitoJwksService cognitoJwksService;

    @Autowired
    private BasePropertySource propertySource;

    /**
     * 
     * @param code
     * @param scheme
     * @param host
     * @return
     */
    public CognitoToken getTokenFromCode(String code, String redirectUrl, String clientId, String clientSecret) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");
        map.add("code", code);
        map.add("redirect_uri", redirectUrl);
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        try {
            String tokenUriString = this.propertySource.getTokenUri();
            ResponseEntity<CognitoToken> result = restTemplate.exchange(tokenUriString,
                    HttpMethod.POST, entity, CognitoToken.class);
            return result.getBody();
        } catch (Exception e) {
            log.error("get accessToken failed with code:{}", code, e);
            return null;
        }
    }

    /**
     * 
     * @param accessToken
     * @return
     */
    public CognitoJWTClaim parseAccessToken(String accessToken) {
        if (accessToken == null) {
            return null;
        }
        try {
            CognitoJwksResponse cognitoJwks = cognitoJwksService.getJwksResponse();
            if (cognitoJwks == null) {
                return null;
            }

            if (StringUtils.isEmpty(accessToken)) {
                return null;
            }
            JWSObject jwsObject = JWSObject.parse(accessToken);
            RSAPublicKey pubKey = getPublickKey(jwsObject.getHeader().getKeyID(), cognitoJwks);
            if (pubKey == null) {
                return null;
            }
            JWSVerifier verifier = new RSASSAVerifier(pubKey);
            if (jwsObject.verify(verifier)) {
                String json = jwsObject.getPayload().toString();
                ObjectMapper mapper = new ObjectMapper();
                CognitoJWTClaim claim = mapper.readValue(json, CognitoJWTClaim.class);
                return claim;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("parse JWT token failed {}", accessToken, e);
            return null;
        }

    }

    private RSAPublicKey getPublickKey(String keyId, CognitoJwksResponse jwks) {
        if (jwks == null || jwks.getKeys() == null || jwks.getKeys().length == 0) {
            return null;
        }
        try {
            for (int index = 0; index < jwks.getKeys().length; index++) {
                CognitoJwkKey key = jwks.getKeys()[index];
                if (key.getKid().equals(keyId)) {
                    BigInteger modulus = new BigInteger(1, Base64.getUrlDecoder().decode(key.getN()));
                    BigInteger pubExp = new BigInteger(1, Base64.getUrlDecoder().decode(key.getE()));

                    KeyFactory keyFactory = KeyFactory.getInstance(key.getKty());
                    RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(modulus, pubExp);
                    return (RSAPublicKey)keyFactory.generatePublic(pubKeySpec);
                }
            }
        } catch (Exception e) {
            log.error("build public key failed {}", e.getMessage(), e);
            return null;
        }
        return null;
    }
}
