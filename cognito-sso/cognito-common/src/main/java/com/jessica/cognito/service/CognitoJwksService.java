package com.jessica.cognito.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jessica.cognito.model.CognitoJwksResponse;
import com.jessica.cognito.property.BasePropertySource;

@Service
@CacheConfig(cacheNames = { "CognitoJwksCache" })
public class CognitoJwksService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BasePropertySource propertySource;

    @Cacheable
    public CognitoJwksResponse getJwksResponse() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(headers);
        String url = String.format("https://cognito-idp.%s.amazonaws.com/%s/.well-known/jwks.json", this.propertySource.getRegion(),
                this.propertySource.getUserPoolId());
        ResponseEntity<CognitoJwksResponse> result = restTemplate.exchange(url, HttpMethod.GET, entity, CognitoJwksResponse.class);
        return result.getBody();
    }
}
