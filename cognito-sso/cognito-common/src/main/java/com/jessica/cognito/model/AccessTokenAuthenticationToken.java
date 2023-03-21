package com.jessica.cognito.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.jessica.cognito.user.UserContext;

import lombok.Getter;

@Getter
public class AccessTokenAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = -5514502450309256494L;

    private final UserContext userContext;

    public AccessTokenAuthenticationToken(UserContext userContext) {
        this(userContext, new ArrayList<GrantedAuthority>());
    }

    public AccessTokenAuthenticationToken(UserContext userContext, List<GrantedAuthority> authorities) {
        super(authorities);
        this.userContext = userContext;
    }

    @Override
    public Object getCredentials() {
        return userContext.getAccessToken();
    }

    @Override
    public Object getPrincipal() {
        return userContext;
    }

}
