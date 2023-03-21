package com.jessica.security.authenticator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jessica.property.CrossDomainPropertySource;

@Component
public class ServiceAccessTokenAuthenticator extends AbstractAccessTokenAuthenticator {

    @Autowired
    private CrossDomainPropertySource propertySource;

    @Override
    public String getRedirectUrl(HttpServletRequest request) {
        return this.propertySource.getServiceHomeUri(this.propertySource.getServiceName());
    }

    @Override
    public boolean support(HttpServletRequest request) {
        return super.support(request) && !this.propertySource.isSSOService();
    }

}
