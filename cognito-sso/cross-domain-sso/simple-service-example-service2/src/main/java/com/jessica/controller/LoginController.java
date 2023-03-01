package com.jessica.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Value("${service.serviceDomain}")
    private String serviceDomain;

    @RequestMapping(method = RequestMethod.GET)
    public void login(@RequestParam(name = "accessToken", required = true) String accessToken,
            HttpServletRequest req, HttpServletResponse res)
            throws Exception {
        String home = serviceDomain + "/home";
        Cookie accessCookie = new Cookie("accessToken", accessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setMaxAge(86400);
        res.addCookie(accessCookie);
        res.sendRedirect(home);
    }
}
