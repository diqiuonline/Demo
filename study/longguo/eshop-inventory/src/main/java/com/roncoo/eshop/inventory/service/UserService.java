package com.roncoo.eshop.inventory.service;

import com.roncoo.eshop.inventory.model.User;

public interface UserService {
    public User findUserInfo();

    User getCacheUserInfo();
}
