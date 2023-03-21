package com.jessica.security.config;

import org.springframework.beans.factory.annotation.Autowired;

import com.jessica.security.authenticator.SSOAccessTokenAuthenticator;
import com.jessica.security.authenticator.SSOLoginCallbackAuthenticator;
import com.jessica.security.filter.CommonAuthFilter;

public class SSOAuthFilterConfig extends AuthFilterConfig {

    @Autowired
    private SSOLoginCallbackAuthenticator callBackAuthenticator;

    @Autowired
    private SSOAccessTokenAuthenticator accessTokenAuthenticator;

    @Override
    public CommonAuthFilter getAuthFilter() {
        CommonAuthFilter authFilter = new CommonAuthFilter(this.getPermitRequests());
        authFilter.setAccessTokenAuthenticator(this.accessTokenAuthenticator);
        authFilter.setLoginCallbackAuthenticator(this.callBackAuthenticator);
        return authFilter;
    }
}
