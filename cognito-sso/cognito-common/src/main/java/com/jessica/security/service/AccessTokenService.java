package com.jessica.security.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import com.jessica.cognito.model.CognitoJWTClaim;
import com.jessica.cognito.model.CognitoToken;
import com.jessica.cognito.service.CognitoTokenService;
import com.jessica.security.utils.RequestUtils;

@Service
public class AccessTokenService {
    @Autowired
    private CognitoTokenService cognitoTokenService;

    public CognitoJWTClaim parseAccessToken(String accessToken) {
        return this.cognitoTokenService.parseAccessToken(accessToken);
    }

    public String getAccessTokenFromCookie(HttpServletRequest request) {
        Cookie accessTokenCookie = WebUtils.getCookie(request, RequestUtils.ACCESS_TOKEN);
        return accessTokenCookie != null ? accessTokenCookie.getValue() : null;
    }

    public String getAccessTokenFromCode(HttpServletRequest request, String redirectUrl, String clientId, String clientSecret) {
        String code = request.getParameter("code");
        if (code == null) {
            return null;
        }
        CognitoToken cognitoToken = this.cognitoTokenService.getTokenFromCode(code, redirectUrl, clientId, clientSecret);
        if (cognitoToken == null) {
            return null;
        }
        return cognitoToken.getAccessToken();
    }

    public void setAccessTokenCookie(String accessToken, String domain, HttpServletResponse response) {
        Cookie accessCookie = new Cookie(RequestUtils.ACCESS_TOKEN, accessToken);
        accessCookie.setHttpOnly(true);
        if (domain != null) {
            accessCookie.setDomain(domain);
        }
        response.addCookie(accessCookie);
    }

    public void deleteAccessTokenCookie(HttpServletResponse response, String domain) {
        Cookie accessCookie = new Cookie(RequestUtils.ACCESS_TOKEN, "deleted");
        accessCookie.setMaxAge(0);
        if (domain != null) {
            accessCookie.setDomain(domain);
        }
        response.addCookie(accessCookie);
    }

}
