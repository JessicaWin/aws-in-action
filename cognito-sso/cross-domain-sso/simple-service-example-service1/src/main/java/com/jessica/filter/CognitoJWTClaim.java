package com.jessica.filter;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CognitoJWTClaim {
    private String sub;
    private String iss;
    private int version;
    @JsonProperty("client_id")
    private String clientId;
    @JsonProperty("origin_jti")
    private String originJti;
    @JsonProperty("token_use")
    private String tokenUse;
    private String scope;
    @JsonProperty("auth_time")
    private long authTime;
    private long exp;
    private long iat;
    private String jti;
    private String username;

    @JsonIgnore
    public boolean isExpired() {
        return this.getExp() * 1000 < new Date().getTime();
    }

}
