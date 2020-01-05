package com.itheima.shop.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.api.IUserService;
import com.itheima.constant.ShopCode;
import com.itheima.exception.CastException;
import com.itheima.shop.mapper.TradeUserMapper;
import com.itheima.shop.pojo.TradeUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
@Component
@Service(interfaceClass = IUserService.class)
public class UserServiceImpl implements IUserService {
    @Autowired
    private TradeUserMapper userMapper;

    /**
     * 查找用户
     * @param userId
     * @return
     */
    @Override
    public TradeUser findOne(Long userId) {
        if (StringUtils.isEmpty(userId)) {
            CastException.cast(ShopCode.SHOP_REQUEST_PARAMETER_VALID);
        }
        TradeUser user = userMapper.selectByPrimaryKey(userId);
        return user;
    }
}
