package com.jessica.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jessica.cognito.service.CognitoParamService;

@RestController
@RequestMapping("/logout")
public class LogoutController {

    @Autowired(required = true)
    private CognitoParamService cognitoParamService;

    @RequestMapping(method = RequestMethod.GET)
    public void logout(@RequestParam(name = "serviceName", required = true) String serviceName,
            HttpServletRequest req, HttpServletResponse res)
            throws Exception {
        Cookie accessCookie = new Cookie("accessToken", "empty");
        accessCookie.setHttpOnly(true);
        accessCookie.setMaxAge(0);
        res.addCookie(accessCookie);
        res.sendRedirect(this.cognitoParamService.getLogoutUri(serviceName));
    }
}
