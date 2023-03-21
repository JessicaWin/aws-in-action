package com.jessica.security.authenticator;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import com.jessica.cognito.model.CognitoJWTClaim;
import com.jessica.cognito.user.User;
import com.jessica.cognito.user.UserService;
import com.jessica.security.service.AccessTokenService;

public abstract class AbstractAuthenticator implements Authenticator {

    @Autowired
    private AccessTokenService accessTokenService;

    @Autowired
    private UserService userService;

    @Override
    public abstract String getAccessToken(HttpServletRequest request);

    @Override
    public Authentication authenticate(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String accessToken = this.getAccessToken(request);
        CognitoJWTClaim jwtClaim = this.accessTokenService.parseAccessToken(accessToken);
        if (jwtClaim == null || jwtClaim.isExpired()) {
            this.failAuthentication(request, response);
            return null;
        }
        User user = this.userService.getUser(jwtClaim.getCognitoUserName());
        if (user == null) {
            this.failAuthentication(request, response);
            return null;
        }
        return this.successAuthentication(user, accessToken, request, response);
    }

}
