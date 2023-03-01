package com.jessica.service;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    /***
     * 
     * @param cognitonUserId
     * @return
     */
    public String getUserId(String cognitonUserId) {
        // in db there should be a table to save the relation of the cognitonUserId and service account userId
        // here should get userId of the service with the given cognitonUserId
        return "test";
    }
}
