package com.jessica.security.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import com.jessica.security.authenticator.Authenticator;
import com.jessica.security.matcher.NotRequestMatcher;
import com.jessica.security.utils.RequestUtils;

import lombok.Setter;

public class CommonAuthFilter extends GenericFilterBean {

    @Setter
    private Authenticator accessTokenAuthenticator;

    @Setter
    private Authenticator loginCallbackAuthenticator;

    @Setter
    private RequestMatcher requiresAuthenticationRequestMatcher;

    public CommonAuthFilter(String... skippedFilterAntPatterns) {
        List<RequestMatcher> matchers = new ArrayList<>();
        for (String antPattern : skippedFilterAntPatterns) {
            matchers.add(new AntPathRequestMatcher(antPattern));
        }
        this.setRequiresAuthenticationRequestMatcher(new NotRequestMatcher(new OrRequestMatcher(matchers)));
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)res;
        if (!requiresAuthentication(request, response)) {
            chain.doFilter(request, response);
            return;
        }
        if (loginCallbackAuthenticator.support(request)) {
            loginCallbackAuthenticator.authenticate(request, response);
            return;
        }
        Authentication authentication = null;
        if (accessTokenAuthenticator.support(request)) {
            authentication = accessTokenAuthenticator.authenticate(request, response);
            // auth verify fail or is login page
            if (authentication == null || RequestUtils.isLogin(request)) {
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return requiresAuthenticationRequestMatcher.matches(request);
    }

}
