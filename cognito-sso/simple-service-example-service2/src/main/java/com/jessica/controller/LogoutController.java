package com.jessica.controller;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logout")
public class LogoutController {

    @Value("${sso.serviceDomain}")
    private String authDomain;

    @Value("${spring.application.name}")
    private String serviceName;

    @RequestMapping(method = RequestMethod.GET)
    public void logout(HttpServletResponse res) throws IOException {
        Cookie accessCookie = new Cookie("accessToken", "empty");
        accessCookie.setHttpOnly(true);
        accessCookie.setMaxAge(0);
        res.addCookie(accessCookie);
        res.sendRedirect(authDomain + "/logout?serviceName=" + serviceName);
    }
}
