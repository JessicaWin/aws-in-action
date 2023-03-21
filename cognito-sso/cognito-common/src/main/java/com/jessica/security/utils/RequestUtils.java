package com.jessica.security.utils;

import javax.servlet.http.HttpServletRequest;

public class RequestUtils {

    private RequestUtils() {
    }

    public static boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("x-requested-with"));
    }

    public static boolean isAuthorizeCallback(HttpServletRequest request) {
        return "/authorize".equals(request.getRequestURI());
    }

    public static boolean isLogin(HttpServletRequest request) {
        return "/login".equals(request.getRequestURI());
    }

    public static String getServiceName(HttpServletRequest request) {
        return request.getParameter(SERVICE_NAME);
    }

    public static String ACCESS_TOKEN = "accessToken";
    public static String SERVICE_NAME = "serviceName";
}
