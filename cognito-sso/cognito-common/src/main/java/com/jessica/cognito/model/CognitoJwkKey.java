package com.jessica.cognito.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CognitoJwkKey {
    private String alg;
    private String e;
    private String kid;
    private String kty;
    private String n;
    private String use;
}