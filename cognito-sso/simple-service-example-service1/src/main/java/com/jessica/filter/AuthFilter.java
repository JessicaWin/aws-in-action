package com.jessica.filter;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.WebUtils;

import com.jessica.context.UserContext;
import com.jessica.service.UserService;

@WebFilter(urlPatterns = { "/*" })
public class AuthFilter implements Filter {

    @Value("${sso.serviceDomain}")
    private String authDomain;

    @Value("${spring.application.name}")
    private String serviceName;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse)response;
        Cookie accessTokenCookie = WebUtils.getCookie(req, "accessToken");
        String accessToken = accessTokenCookie == null ? null : accessTokenCookie.getValue();
        if ("/logout".equals(req.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }
        if ("/login".equals(req.getRequestURI()) && req.getParameter("accessToken") != null) {
            accessToken = req.getParameter("accessToken");
        }

        if (accessToken == null) {
            String redirectUrlString = authDomain + "/login?serviceName=" + serviceName;
            res.sendRedirect(redirectUrlString);
            return;
        }
        CognitoJWTClaim claim = this.verifyToken(accessToken);
        if (claim == null) {
            res.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        } else if (claim.isExpired()) {
            String redirectUrlString = authDomain + "/login?serviceName=" + serviceName;
            res.sendRedirect(redirectUrlString);
            return;
        }
        String userIdString = this.userService.getUserId(claim.getUsername());
        UserContext.reset(UserContext.builder().userId(userIdString).build());
        chain.doFilter(request, response);
    }

    private CognitoJWTClaim verifyToken(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        String url = authDomain + "/authorize?&accessToken=" + accessToken;
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(headers);
        ResponseEntity<CognitoJWTClaim> result = restTemplate.exchange(url,
                HttpMethod.GET, entity, CognitoJWTClaim.class);
        return result.getBody();
    }
}
