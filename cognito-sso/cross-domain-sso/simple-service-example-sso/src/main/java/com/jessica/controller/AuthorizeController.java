package com.jessica.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jessica.cognito.model.CognitoJWTClaim;
import com.jessica.cognito.service.CognitoService;

@RestController
@RequestMapping("/authorize")
public class AuthorizeController {

    @Autowired
    private CognitoService accessTokenService;

    @RequestMapping(method = RequestMethod.GET)
    public CognitoJWTClaim authorize(@RequestParam(name = "accessToken", required = true) String accessToken,
            HttpServletRequest req, HttpServletResponse res)
            throws Exception {
        return accessTokenService.verityAccessToken(accessToken);
    }
}
