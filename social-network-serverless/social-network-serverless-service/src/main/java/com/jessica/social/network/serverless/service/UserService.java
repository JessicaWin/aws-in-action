package com.jessica.social.network.serverless.service;

import com.jessica.social.network.serverless.bo.UserBo;

public interface UserService {
    /**
     *
     * @param userBo
     */
    void createUser(UserBo userBo);

    /**
     *
     * @param id
     */
    void deleteById(String id);

    /**
     *
     * @param userBo
     */
    void updateUser(UserBo userBo);

    /**
     *
     * @param id
     * @return
     */
    UserBo getById(String id);
}
