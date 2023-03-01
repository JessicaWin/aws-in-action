package com.jessica.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jessica.cognito.model.CognitoAccessToken;
import com.jessica.cognito.model.CognitoJWTClaim;
import com.jessica.cognito.service.CognitoParamService;
import com.jessica.cognito.service.CognitoService;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired(required = true)
    private CognitoService accessTokenService;

    @Autowired(required = true)
    private CognitoParamService cognitoClientService;

    @RequestMapping(method = RequestMethod.GET)
    public void login(@RequestParam(name = "serviceName", required = true) String serviceName,
            @CookieValue(name = "accessToken", required = false) String accessToken,
            @RequestParam(name = "code", required = false) String code,
            HttpServletRequest req, HttpServletResponse res)
            throws Exception {
        String serviceRedirectUrl = this.cognitoClientService.getServiceLoginRedirectUri(serviceName);
        if (serviceRedirectUrl == null) {
            res.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }
        if (code != null) {
            CognitoAccessToken token = accessTokenService.getTokenFromCode(code, serviceName);
            Cookie accessCookie = new Cookie("accessToken", token.getAccessToken());
            accessCookie.setHttpOnly(true);
            accessCookie.setMaxAge(86400);
            res.addCookie(accessCookie);
            res.sendRedirect(serviceRedirectUrl + "?accessToken=" + token.getAccessToken());
            return;
        }
        if (accessToken != null) {
            CognitoJWTClaim claim = this.accessTokenService.verityAccessToken(accessToken);
            if (claim != null && !claim.isExpired()) {
                res.sendRedirect(serviceRedirectUrl + "?accessToken=" + accessToken);
            } else {
                res.sendRedirect(this.cognitoClientService.getLoginUri(serviceName));
            }
            return;
        }
        res.sendRedirect(this.cognitoClientService.getLoginUri(serviceName));
        return;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/test")
    public String checkCookie() {
        return "test";
    }
}
