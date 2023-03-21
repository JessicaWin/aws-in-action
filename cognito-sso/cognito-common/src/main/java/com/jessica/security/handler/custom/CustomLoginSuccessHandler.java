package com.jessica.security.handler.custom;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CustomLoginSuccessHandler {
    public void onLogin(HttpServletRequest request, HttpServletResponse response);
}
