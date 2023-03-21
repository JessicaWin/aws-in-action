package com.jessica.security.authenticator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jessica.cognito.service.SameDomainPropertySource;
import com.jessica.security.service.AccessTokenService;

@Component
public class LoginCallbackAuthenticator extends AbstractLoginCallbackAuthenticator {

    @Autowired
    private AccessTokenService accessTokenService;

    @Autowired
    private SameDomainPropertySource propertySource;

    @Override
    public String getAccessToken(HttpServletRequest request) {
        String redirectUrlString = this.propertySource.getServiceAuthorizeUri();
        return this.accessTokenService.getAccessTokenFromCode(request, redirectUrlString, propertySource.getClientId(),
                propertySource.getClientSecret());
    }

    @Override
    public String getRedirectUrl(HttpServletRequest request) {
        return this.propertySource.getServiceHomeUri();
    }

}
