package com.jessica.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jessica.cognito.user.UserContext;

@RestController
@RequestMapping("/home")
public class HomeController {

    @RequestMapping(method = RequestMethod.GET)
    public String home() {
        return "current login user is (" + UserContext.getInstance().getUser().getUserId() + ")";
    }
}
