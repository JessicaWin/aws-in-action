package com.jessica.handler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jessica.cognito.user.UserContext;
import com.jessica.property.CrossDomainPropertySource;
import com.jessica.security.handler.custom.CustomLoginSuccessHandler;

@Service
public class SrviceLoginHandler implements CustomLoginSuccessHandler {

    @Autowired
    private CrossDomainPropertySource propertySource;

    @Override
    public void onLogin(HttpServletRequest request, HttpServletResponse response) {
        Cookie accessCookie = new Cookie("X-SERVICE-USERID", UserContext.getInstance().getUser().getUserId());
        String domain = this.propertySource.getCookieDomain();
        accessCookie.setHttpOnly(true);
        if (domain != null) {
            accessCookie.setDomain(domain);
        }
        response.addCookie(accessCookie);
    }
}
