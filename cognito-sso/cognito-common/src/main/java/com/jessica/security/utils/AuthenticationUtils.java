package com.jessica.security.utils;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationUtils {
    private AuthenticationUtils() {

    }

    public static void redirectRequest(HttpServletRequest request, HttpServletResponse response, String targetUrl)
            throws IOException {
        if (RequestUtils.isAjaxRequest(request)) {
            response.setHeader("REDIRECT", "REDIRECT");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            response.sendRedirect(targetUrl);
        }
    }

    /**
     * return parent domain: service.jessica.com.cn -> .jessica.com.cn
     *
     * @param host
     * @return
     */
    public static String getCookieDomain(String host) {
        host = host.split(":")[0];
        if (isIPAddr(host)) {
            return host;
        }
        String domain = host;
        String hostTokens[] = host.split("\\.", 2);
        if (hostTokens.length > 1) {
            domain = hostTokens[1];
        }
        return domain;
    }

    public static boolean isIPAddr(String host) {
        String regex = "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(host).matches();
    }

}
