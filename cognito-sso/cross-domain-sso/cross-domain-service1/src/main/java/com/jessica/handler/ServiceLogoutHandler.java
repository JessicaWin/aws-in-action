package com.jessica.handler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jessica.property.CrossDomainPropertySource;
import com.jessica.security.handler.custom.CustomLogoutHandler;

@Service
public class ServiceLogoutHandler implements CustomLogoutHandler {

    @Autowired
    private CrossDomainPropertySource propertySource;

    @Override
    public void onLogout(HttpServletRequest request, HttpServletResponse response) {
        Cookie accessCookie = new Cookie("X-SERVICE-USERID", "deleted");
        String domain = this.propertySource.getCookieDomain();
        accessCookie.setMaxAge(0);
        if (domain != null) {
            accessCookie.setDomain(domain);
        }
        response.addCookie(accessCookie);
    }

}
