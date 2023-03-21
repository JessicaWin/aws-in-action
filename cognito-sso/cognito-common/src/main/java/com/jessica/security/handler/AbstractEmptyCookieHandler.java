package com.jessica.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.jessica.security.utils.AuthenticationUtils;

public abstract class AbstractEmptyCookieHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        String loginUrl = this.getRedirectUrl(request);
        AuthenticationUtils.redirectRequest(request, response, loginUrl);
    }

    public abstract String getRedirectUrl(HttpServletRequest request);
}
