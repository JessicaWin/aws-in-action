package com.jessica.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jessica.cognito.user.User;
import com.jessica.cognito.user.UserService;
import com.jessica.property.CrossDomainPropertySource;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private CrossDomainPropertySource propertySource;

    @Override
    public User getUser(String cognitoUserName) {
        return User.builder()
                .cognitoUserName(cognitoUserName)
                .userId(this.propertySource.getServiceName() + ":" + cognitoUserName).build();
    }

}
