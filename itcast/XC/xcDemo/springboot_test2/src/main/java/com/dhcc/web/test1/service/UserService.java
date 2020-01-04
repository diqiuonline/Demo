package com.dhcc.web.test1.service;

import com.dhcc.web.test1.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: 李锦卓
 * Time: 2019/2/20 23:49
 */
@Service
@Transactional
public class UserService {


    @Autowired
    private UserMapper userMapper;

    public void save(String username, String password) {
        userMapper.insert(username, password);
        //int i = 1 / 0;
    }
}