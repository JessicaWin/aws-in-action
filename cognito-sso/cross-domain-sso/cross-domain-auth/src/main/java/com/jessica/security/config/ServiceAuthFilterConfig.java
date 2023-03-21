package com.jessica.security.config;

import org.springframework.beans.factory.annotation.Autowired;

import com.jessica.security.authenticator.ServiceAccessTokenAuthenticator;
import com.jessica.security.authenticator.ServiceLoginCallbackAuthenticator;
import com.jessica.security.filter.CommonAuthFilter;

public class ServiceAuthFilterConfig extends AuthFilterConfig {

    @Autowired
    private ServiceLoginCallbackAuthenticator callBackAuthenticator;

    @Autowired
    private ServiceAccessTokenAuthenticator accessTokenAuthenticator;

    @Override
    public CommonAuthFilter getAuthFilter() {
        CommonAuthFilter authFilter = new CommonAuthFilter(this.getPermitRequests());
        authFilter.setAccessTokenAuthenticator(this.accessTokenAuthenticator);
        authFilter.setLoginCallbackAuthenticator(this.callBackAuthenticator);
        return authFilter;
    }
}
