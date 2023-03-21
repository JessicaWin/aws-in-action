package com.jessica.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.jessica.security.filter.CommonAuthFilter;
import com.jessica.security.handler.AbstractUserLogoutHandler;

public abstract class AuthFilterConfig extends WebSecurityConfigurerAdapter {
    private static final String[] PERMIT_REQUESTS = new String[] {
            "/favicon.ico",
            "/logout",
            "/error",
            "/test" };

    private static final String[] DENY_REQUESTS = new String[] {
            "/actuator/**"
    };

    private static final String LOGOUT_URL = "/logout";

    protected String[] getPermitRequests() {
        return PERMIT_REQUESTS;
    }

    protected String[] getDenyRequests() {
        return DENY_REQUESTS;
    }

    @Autowired
    private AbstractUserLogoutHandler userLogoutHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(getPermitRequests())
                .permitAll()
                .antMatchers(getDenyRequests())
                .denyAll()
                .anyRequest()
                .authenticated()
                .and()
                .addFilterBefore(getAuthFilter(), UsernamePasswordAuthenticationFilter.class);

        http.logout()
                .logoutRequestMatcher(
                        new AntPathRequestMatcher(LOGOUT_URL))
                .addLogoutHandler(userLogoutHandler)
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        http.headers().frameOptions().sameOrigin();
        http.csrf().disable();
    }

    public abstract CommonAuthFilter getAuthFilter();

}
