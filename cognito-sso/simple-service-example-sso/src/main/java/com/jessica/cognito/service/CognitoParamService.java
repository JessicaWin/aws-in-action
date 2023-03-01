package com.jessica.cognito.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class CognitoParamService {

    @Autowired
    private Environment environment;

    public String getClientId(String serviceName) {
        return environment.getProperty(String.join("", "cognito.clientId.", serviceName));
    }

    public String getClientSecret(String serviceName) {
        return environment.getProperty(String.join("", "cognito.clientSecret.", serviceName));
    }

    public String getServiceLoginRedirectUri(String serviceName) {
        return environment.getProperty(String.join(".", serviceName, "serviceDomain")) + "/login";
    }

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
        return environment.getProperty("cognito.userPoolTokenUri");
    }

    public String getLoginUri(String serviceName) {
        String ssoLogInUrl = this.getSSORedirectUri(serviceName);

        return String.join("", environment.getProperty("cognito.userPoolDomain"), "/login?client_id=", this.getClientId(serviceName),
                "&response_type=code&scope=aws.cognito.signin.user.admin+openid+profile", "&redirect_uri=", ssoLogInUrl);
    }

    public String getLogoutUri(String serviceName) {
        String ssoLogInUrl = this.getSSORedirectUri(serviceName);
        return String.join("", environment.getProperty("cognito.userPoolDomain"), "/logout?client_id=", this.getClientId(serviceName),
                "&response_type=code&scope=aws.cognito.signin.user.admin+openid+profile", "&redirect_uri=", ssoLogInUrl);
    }

    public String getSSORedirectUri(String serviceName) {
        return String.join("", environment.getProperty("sso.serviceDomain"), "/login?serviceName=", serviceName);
    }
}
