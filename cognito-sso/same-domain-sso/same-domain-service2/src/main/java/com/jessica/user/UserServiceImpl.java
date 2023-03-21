package com.jessica.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jessica.cognito.service.SameDomainPropertySource;
import com.jessica.cognito.user.User;
import com.jessica.cognito.user.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SameDomainPropertySource propertySource;

    @Override
    public User getUser(String cognitoUserName) {
        return User.builder()
                .cognitoUserName(cognitoUserName)
                .userId(this.propertySource.getServiceName() + ":" + cognitoUserName).build();
    }

}
