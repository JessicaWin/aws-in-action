package com.jessica.cognito.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String userName;
    private String userId;
    private String email;
    private String cognitoUserName;
}
