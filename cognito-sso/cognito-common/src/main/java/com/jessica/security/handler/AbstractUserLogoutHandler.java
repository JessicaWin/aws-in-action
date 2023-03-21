package com.jessica.security.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.jessica.cognito.property.BasePropertySource;
import com.jessica.security.handler.custom.CustomLogoutHandler;
import com.jessica.security.service.AccessTokenService;
import com.jessica.security.utils.AuthenticationUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractUserLogoutHandler implements LogoutHandler {
    @Autowired
    private AccessTokenService accessTokenService;

    @Autowired
    private BasePropertySource propertySource;

    @Autowired(required = false)
    private CustomLogoutHandler customLogoutHandler;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication auth) {
        String domain = this.propertySource.getCookieDomain();
        accessTokenService.deleteAccessTokenCookie(response, domain);
        response.addHeader("Content-type", MediaType.TEXT_HTML_VALUE);
        if (this.customLogoutHandler != null) {
            this.customLogoutHandler.onLogout(request, response);
        }
        String redirectUrl = this.getRedirectUrl(request);
        try {
            AuthenticationUtils.redirectRequest(request, response, redirectUrl);
        } catch (IOException e) {
            log.warn("Fail to redirect to " + redirectUrl, e);
        }
    }

    public abstract String getRedirectUrl(HttpServletRequest request);
}
