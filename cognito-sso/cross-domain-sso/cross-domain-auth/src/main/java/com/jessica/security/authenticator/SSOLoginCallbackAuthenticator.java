package com.jessica.security.authenticator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jessica.property.CrossDomainPropertySource;
import com.jessica.security.service.AccessTokenService;
import com.jessica.security.utils.RequestUtils;

@Component
public class SSOLoginCallbackAuthenticator extends AbstractLoginCallbackAuthenticator {

    @Autowired
    private AccessTokenService accessTokenService;

    @Autowired
    private CrossDomainPropertySource propertySource;

    @Override
    public String getAccessToken(HttpServletRequest request) {
        String serviceName = RequestUtils.getServiceName(request);
        String redirectUrlString = this.propertySource.getSSOAuthorizeUri(serviceName);
        return this.accessTokenService.getAccessTokenFromCode(request, redirectUrlString, propertySource.getClientId(serviceName),
                propertySource.getClientSecret(serviceName));
    }

    @Override
    public String getRedirectUrl(HttpServletRequest request) {
        return this.propertySource.getServiceAuthorizeUri(RequestUtils.getServiceName(request));
    }

    @Override
    public boolean support(HttpServletRequest request) {
        return super.support(request) && this.propertySource.isSSOService();
    }

}
