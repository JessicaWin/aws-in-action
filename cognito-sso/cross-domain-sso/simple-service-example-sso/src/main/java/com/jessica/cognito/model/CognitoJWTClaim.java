package com.jessica.cognito.model;

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
    String sub;
    String iss;
    int version;
    @JsonProperty("client_id")
    String clientId;
    @JsonProperty("origin_jti")
    String originJti;
    @JsonProperty("token_use")
    String tokenUse;
    String scope;
    @JsonProperty("auth_time")
    long authTime;
    long exp;
    long iat;
    String jti;
    String username;

    @JsonIgnore
    public boolean isExpired() {
        return this.getExp() * 1000 < new Date().getTime();
    }

}
