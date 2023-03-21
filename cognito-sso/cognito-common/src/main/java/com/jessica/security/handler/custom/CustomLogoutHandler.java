package com.jessica.security.handler.custom;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CustomLogoutHandler {
    public void onLogout(HttpServletRequest request, HttpServletResponse response);
}
