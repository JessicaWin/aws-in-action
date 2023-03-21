package com.jessica.security.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jessica.property.CrossDomainPropertySource;
import com.jessica.security.utils.RequestUtils;

@Component
public class UserLogoutHandler extends AbstractUserLogoutHandler {

    @Autowired
    private CrossDomainPropertySource propertySource;

    @Override
    public String getRedirectUrl(HttpServletRequest request) {
        String serviceName = propertySource.isSSOService() ? RequestUtils.getServiceName(request)
                : this.propertySource.getServiceName();
        return this.propertySource.isSSOService() ? this.propertySource.getCognitoLogoutUri(serviceName)
                : this.propertySource.getSSOLogoutUri(serviceName);
    }

}
