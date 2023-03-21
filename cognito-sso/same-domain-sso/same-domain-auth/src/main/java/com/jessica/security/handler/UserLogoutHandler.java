package com.jessica.security.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jessica.cognito.service.SameDomainPropertySource;

@Component
public class UserLogoutHandler extends AbstractUserLogoutHandler {

    @Autowired
    private SameDomainPropertySource propertySource;

    @Override
    public String getRedirectUrl(HttpServletRequest request) {
        return this.propertySource.getCognitoLogoutUri();
    }

}
