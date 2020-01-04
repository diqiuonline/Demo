package com.donghua.service.impl;

import com.donghua.mapper.UserMapper;
import com.donghua.pojo.User;
import com.donghua.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: 李锦卓
 * Time: 2018/11/15 17:24
 */
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public void setUser(User user) {
        userMapper.insert(user);
    }
}