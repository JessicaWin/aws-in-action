package com.jessica.cognito.property;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public class BasePropertySource {

    @Autowired
    private Environment environment;

    public String getRegion() {
        return environment.getProperty("cognito.region");
    }

    public String getUserPoolId() {
        return environment.getProperty("cognito.userPoolId");
    }

    public String getUserPoolDomain() {
        return environment.getProperty("cognito.userPoolDomain");
    }

    public String getTokenUri() {
        return environment.getProperty("cognito.userPoolDomain") + "/oauth2/token";
    }

    public String getRevokeTokenUri() {
        return environment.getProperty("cognito.userPoolDomain") + "/oauth2/revoke";
    }

    public String getCookieDomain() {
        return environment.getProperty("service.cookie.domain");
    }

    public String getServiceDomain() {
        return environment.getProperty("service.domain");
    }

    public String getServiceName() {
        return environment.getProperty("spring.application.name");
    }

    public boolean isSSOService() {
        return environment.getProperty("service.isSSO", Boolean.class, false);
    }
}
