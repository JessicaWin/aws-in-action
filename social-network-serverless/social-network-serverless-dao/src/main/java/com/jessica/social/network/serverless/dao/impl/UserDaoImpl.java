package com.jessica.social.network.serverless.dao.impl;

import com.jessica.social.network.serverless.dao.UserDao;
import com.jessica.social.network.serverless.item.UserItem;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl extends BasicOperationDaoImpl<UserItem> implements UserDao {
}
