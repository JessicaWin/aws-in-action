package com.jessica.security.authenticator;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.jessica.cognito.user.User;
import com.jessica.security.handler.LoginSuccessHandler;
import com.jessica.security.utils.AuthenticationUtils;

public abstract class AbstractLoginCallbackAuthenticator extends AbstractAuthenticator {

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    public abstract String getRedirectUrl(HttpServletRequest request);

    @Override
    public Authentication successAuthentication(User user, String accessToken, HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {
        Authentication authentication = super.successAuthentication(user, accessToken, request, response);
        this.loginSuccessHandler.onAuthenticationSuccess(request, response, SecurityContextHolder.getContext().getAuthentication());
        String url = this.getRedirectUrl(request);
        AuthenticationUtils.redirectRequest(request, response, url);
        return authentication;
    }

    @Override
    public void failAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        super.failAuthentication(request, response);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    @Override
    public boolean support(HttpServletRequest request) {
        return "/authorize".equals(request.getRequestURI());
    }
}