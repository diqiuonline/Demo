package com.roncoo.eshop.inventory.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.roncoo.eshop.inventory.dao.RedisDao;
import com.roncoo.eshop.inventory.mapper.UserMapper;
import com.roncoo.eshop.inventory.model.User;
import com.roncoo.eshop.inventory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisDao redisDao;
    @Override
    public User findUserInfo() {
        return userMapper.findUserInfo();
    }

    @Override
    public User getCacheUserInfo() {
        redisDao.set("cache_user_lisi","{\"name\":\"lisi\",\"age\":28}");
        String cache_user_list = redisDao.get("cache_user_lisi");
        User user = JSONObject.parseObject(cache_user_list, User.class);
        return user;
    }
}
