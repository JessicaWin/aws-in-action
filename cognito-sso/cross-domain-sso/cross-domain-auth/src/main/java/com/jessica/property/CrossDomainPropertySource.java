package com.jessica.property;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.jessica.cognito.property.BasePropertySource;
import com.jessica.cognito.user.UserContext;

@Component
public class CrossDomainPropertySource extends BasePropertySource {

    @Autowired
    private Environment environment;

    public String getClientId(String serviceName) {
        return environment.getProperty(String.join("", "cognito.clientId." + serviceName));
    }

    public String getClientSecret(String serviceName) {
        return environment.getProperty(String.join("", "cognito.clientSecret." + serviceName));
    }

    public String getCognitoLoginUri(String serviceName) {
        String redirectUrl = this.getSSOAuthorizeUri(serviceName);
        return String.join("", environment.getProperty("cognito.userPoolDomain"), "/login?client_id=", this.getClientId(serviceName),
                "&response_type=code&scope=aws.cognito.signin.user.admin+openid+profile", "&redirect_uri=", redirectUrl);
    }

    public String getCognitoLogoutUri(String serviceName) {
        String redirectUrl = this.getSSOAuthorizeUri(serviceName);
        return String.join("", environment.getProperty("cognito.userPoolDomain"), "/logout?client_id=", this.getClientId(serviceName),
                "&response_type=code&scope=aws.cognito.signin.user.admin+openid+profile", "&redirect_uri=", redirectUrl);
    }

    public String getServiceLoginUri(String serviceName) {
        return String.join("", environment.getProperty(serviceName + ".domain"), "/login");
    }

    public String getServiceHomeUri(String serviceName) {
        return String.join("", environment.getProperty(serviceName + ".domain"), "/home");
    }

    public String getServiceAuthorizeUri(String serviceName) {
        return String.join("", environment.getProperty(serviceName + ".domain"), "/authorize?accessToken=",
                UserContext.getInstance().getAccessToken());
    }

    public String getSSOAuthorizeUri(String serviceName) {
        return String.join("", environment.getProperty("sso.domain"), "/authorize?serviceName=", serviceName);
    }

    public String getSSOLoginUri(String serviceName) {
        return String.join("", environment.getProperty("sso.domain"), "/login?serviceName=", serviceName);
    }

    public String getSSOLogoutUri(String serviceName) {
        return String.join("", environment.getProperty("sso.domain"), "/logout?serviceName=", serviceName);
    }
}
