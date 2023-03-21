package com.jessica.security.config;

import org.springframework.beans.factory.annotation.Autowired;

import com.jessica.security.authenticator.AccessTokenAuthenticator;
import com.jessica.security.authenticator.LoginCallbackAuthenticator;
import com.jessica.security.filter.CommonAuthFilter;

public class ServiceAuthFilterConfig extends AuthFilterConfig {

    @Autowired
    private LoginCallbackAuthenticator callBackAuthenticator;

    @Autowired
    private AccessTokenAuthenticator accessTokenAuthenticator;

    @Override
    public CommonAuthFilter getAuthFilter() {
        CommonAuthFilter authFilter = new CommonAuthFilter(this.getPermitRequests());
        authFilter.setAccessTokenAuthenticator(this.accessTokenAuthenticator);
        authFilter.setLoginCallbackAuthenticator(this.callBackAuthenticator);
        return authFilter;
    }
}
