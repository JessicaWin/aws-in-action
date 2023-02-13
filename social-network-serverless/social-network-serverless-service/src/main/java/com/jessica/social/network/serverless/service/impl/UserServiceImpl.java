package com.jessica.social.network.serverless.service.impl;

import com.jessica.social.network.serverless.bo.UserBo;
import com.jessica.social.network.serverless.dao.UserDao;
import com.jessica.social.network.serverless.item.UserItem;
import com.jessica.social.network.serverless.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;


    @Override
    public void createUser(UserBo userBo) {
        if (userBo == null) {
            return;
        }
        this.userDao.save(userBo.toItem());
    }

    @Override
    public void deleteById(String id) {
        if (id == null) {
            return;
        }
        this.userDao.delete(UserItem.builder().id(id).build());
    }

    @Override
    public void updateUser(UserBo userBo) {
        if (userBo == null) {
            return;
        }
        this.userDao.save(userBo.toItem());
    }

    @Override
    public UserBo getById(String id) {
        if (id == null) {
            return null;
        }
        return UserBo.fromItem(this.userDao.load(UserItem.builder().id(id).build()));
    }
}
