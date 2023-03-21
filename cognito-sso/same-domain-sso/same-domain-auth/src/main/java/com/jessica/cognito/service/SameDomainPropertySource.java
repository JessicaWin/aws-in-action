package com.jessica.cognito.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.jessica.cognito.property.BasePropertySource;

@Component
public class SameDomainPropertySource extends BasePropertySource {

    @Autowired
    private Environment environment;

    public String getClientId() {
        return environment.getProperty(String.join("", "cognito.clientId"));
    }

    public String getClientSecret() {
        return environment.getProperty(String.join("", "cognito.clientSecret"));
    }

    public String getCognitoLoginUri() {
        String redirectUrl = this.getServiceAuthorizeUri();
        return String.join("", environment.getProperty("cognito.userPoolDomain"), "/login?client_id=", this.getClientId(),
                "&response_type=code&scope=aws.cognito.signin.user.admin+openid+profile", "&redirect_uri=", redirectUrl);
    }

    public String getCognitoLogoutUri() {
        String redirectUrl = this.getServiceAuthorizeUri();
        return String.join("", environment.getProperty("cognito.userPoolDomain"), "/logout?client_id=", this.getClientId(),
                "&response_type=code&scope=aws.cognito.signin.user.admin+openid+profile", "&redirect_uri=", redirectUrl);
    }

    public String getServiceAuthorizeUri() {
        return String.join("", environment.getProperty("service.domain"), "/authorize");
    }

    public String getServiceHomeUri() {
        return String.join("", environment.getProperty("service.domain"), "/home");
    }

    public String getServiceLoginUri() {
        return String.join("", environment.getProperty("service.domain"), "/login");
    }

}
