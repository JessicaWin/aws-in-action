package com.jessica.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.jessica.cognito.property.BasePropertySource;
import com.jessica.security.handler.custom.CustomLoginSuccessHandler;
import com.jessica.security.service.AccessTokenService;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private AccessTokenService accessTokenService;

    @Autowired
    private BasePropertySource propertySource;

    @Autowired(required = false)
    private CustomLoginSuccessHandler customLoginSuccessHandler;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        String cookieVal = authentication.getCredentials().toString();
        String domain = this.propertySource.getCookieDomain();
        accessTokenService.setAccessTokenCookie(cookieVal, domain, response);
        if (this.customLoginSuccessHandler != null) {
            this.customLoginSuccessHandler.onLogin(request, response);
        }
    }

}
