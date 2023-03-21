package com.jessica.security.authenticator;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import com.jessica.cognito.user.User;
import com.jessica.security.exception.RestAPIInvalidException;
import com.jessica.security.handler.AbstractEmptyCookieHandler;
import com.jessica.security.handler.RestAPIAuthFailHandler;
import com.jessica.security.service.AccessTokenService;
import com.jessica.security.utils.AuthenticationUtils;
import com.jessica.security.utils.RequestUtils;

public abstract class AbstractAccessTokenAuthenticator extends AbstractAuthenticator {

    @Autowired
    private AccessTokenService accessTokenService;

    @Autowired
    private RestAPIAuthFailHandler restFailureHandler;

    @Autowired
    private AbstractEmptyCookieHandler emptyCookieHandler;

    @Override
    public String getAccessToken(HttpServletRequest request) {
        return this.accessTokenService.getAccessTokenFromCookie(request);
    }

    public abstract String getRedirectUrl(HttpServletRequest request);

    @Override
    public Authentication successAuthentication(User user, String accessToken, HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {
        Authentication authentication = super.successAuthentication(user, accessToken, request, response);
        if (RequestUtils.isLogin(request)) {
            String url = this.getRedirectUrl(request);
            AuthenticationUtils.redirectRequest(request, response, url);
        }
        return authentication;
    }

    @Override
    public void failAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        super.failAuthentication(request, response);
        if (RequestUtils.isAjaxRequest(request)) {
            this.restFailureHandler.onAuthenticationFailure(request, response, new RestAPIInvalidException("rest authentication failed"));
        } else {
            this.emptyCookieHandler.onAuthenticationFailure(request, response, null);
        }
    }

    @Override
    public boolean support(HttpServletRequest request) {
        return !RequestUtils.isAuthorizeCallback(request);
    }
}
