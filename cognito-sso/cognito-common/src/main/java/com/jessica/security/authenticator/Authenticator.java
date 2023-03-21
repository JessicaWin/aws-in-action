package com.jessica.security.authenticator;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.jessica.cognito.model.AccessTokenAuthenticationToken;
import com.jessica.cognito.user.User;
import com.jessica.cognito.user.UserContext;

public interface Authenticator {

    /**
     * 
     * @param request
     * @return
     */
    String getAccessToken(HttpServletRequest request);

    /**
     * 
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    Authentication authenticate(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;

    /**
     * 
     * @param request
     * @param response
     * @param ae
     * @throws IOException
     * @throws ServletException
     */
    default void failAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }

    /**
     * 
     * @param request
     * @param response
     * @param userContext
     * @throws IOException
     * @throws ServletException
     */
    default Authentication successAuthentication(User user, String accessToken, HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {
        UserContext.reset(UserContext.builder().user(user).accessToken(accessToken).build());
        Authentication authentication = new AccessTokenAuthenticationToken(UserContext.getInstance());
        authentication.setAuthenticated(true);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        return authentication;
    }

    /**
     * 
     * @param request
     * @return
     */
    default boolean support(HttpServletRequest request) {
        return false;
    }

}
