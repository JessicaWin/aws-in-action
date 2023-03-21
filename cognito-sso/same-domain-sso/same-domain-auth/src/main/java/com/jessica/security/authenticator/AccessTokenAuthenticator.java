package com.jessica.security.authenticator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jessica.cognito.service.SameDomainPropertySource;

@Component
public class AccessTokenAuthenticator extends AbstractAccessTokenAuthenticator {

    @Autowired
    private SameDomainPropertySource propertySource;

    @Override
    public String getRedirectUrl(HttpServletRequest request) {
        return this.propertySource.getServiceHomeUri();
    }
}
