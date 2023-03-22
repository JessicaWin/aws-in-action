package com.jessica.cognito.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CognitoJwksResponse {
    private CognitoJwkKey[] keys;
}
