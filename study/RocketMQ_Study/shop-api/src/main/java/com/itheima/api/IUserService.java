package com.itheima.api;

import com.itheima.shop.pojo.TradeUser;

public interface IUserService {
    //查找用户
    TradeUser findOne(Long userId);
}
