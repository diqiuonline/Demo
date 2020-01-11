package com.itheima.shop.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.api.IUserService;
import com.itheima.constant.ShopCode;
import com.itheima.entity.Result;
import com.itheima.exception.CastException;
import com.itheima.shop.mapper.TradeUserMapper;
import com.itheima.shop.mapper.TradeUserMoneyLogMapper;
import com.itheima.shop.pojo.TradeUser;
import com.itheima.shop.pojo.TradeUserMoneyLog;
import com.itheima.shop.pojo.TradeUserMoneyLogExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

@Component
@Service(interfaceClass = IUserService.class)
public class UserServiceImpl implements IUserService {
    @Autowired
    private TradeUserMapper userMapper;
    @Autowired
    private TradeUserMoneyLogMapper userMoneyLogMapper;
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

    /**
     * 扣减余额
     *
     * @param userMoneyLog
     * @return
     */
    @Override
    public Result updateMoneyPaid(TradeUserMoneyLog userMoneyLog) {
        //1 校验参数是否合法
        if (StringUtils.isEmpty(userMoneyLog) || StringUtils.isEmpty(userMoneyLog.getUserId())
                || StringUtils.isEmpty(userMoneyLog.getOrderId())
                || userMoneyLog.getUseMoney().compareTo(BigDecimal.ZERO) <= 0 || StringUtils.isEmpty(userMoneyLog.getUseMoney())) {
            CastException.cast(ShopCode.SHOP_REQUEST_PARAMETER_VALID);
        }
        //2 查询我们的额订单余额使用日志情况
        TradeUserMoneyLogExample userMoneyLogExample = new TradeUserMoneyLogExample();
        TradeUserMoneyLogExample.Criteria criteria = userMoneyLogExample.createCriteria();
        criteria.andOrderIdEqualTo(userMoneyLog.getOrderId());
        criteria.andUserIdEqualTo(userMoneyLog.getUserId());
        int i = userMoneyLogMapper.countByExample(userMoneyLogExample);
        //3 扣减余额
        if (userMoneyLog.getMoneyLogType().intValue() == ShopCode.SHOP_USER_MONEY_PAID.getCode().intValue()) {
            if (i > 0) {
                //已经付款
                CastException.cast(ShopCode.SHOP_ORDER_PAY_STATUS_IS_PAY);
            }
            TradeUser user = userMapper.selectByPrimaryKey(userMoneyLog.getUserId());
            user.setUserMoney(new BigDecimal(user.getUserMoney()).subtract(userMoneyLog.getUseMoney()).longValue());
            userMapper.updateByPrimaryKey(user);
        }
        //4 回退余额
        if (userMoneyLog.getMoneyLogType().intValue() == ShopCode.SHOP_USER_MONEY_REFUND.getCode().intValue()) {
            if (i < 0) {
                //没有付过款
                CastException.cast(ShopCode.SHOP_ORDER_PAY_STATUS_NO_PAY);
            }
            //防止多次退款
            TradeUserMoneyLogExample userMoneyLogExample2 = new TradeUserMoneyLogExample();
            TradeUserMoneyLogExample.Criteria criteria1 = userMoneyLogExample2.createCriteria();
            criteria1.andOrderIdEqualTo(userMoneyLog.getOrderId());
            criteria1.andUserIdEqualTo(userMoneyLog.getUserId());
            criteria1.andMoneyLogTypeEqualTo(ShopCode.SHOP_USER_MONEY_REFUND.getCode());
            int i1 = userMoneyLogMapper.countByExample(userMoneyLogExample2);
            if (i1 > 0) {
                CastException.cast(ShopCode.SHOP_USER_MONEY_REFUND_ALREADY);
            }
            //正式退款
            TradeUser user = userMapper.selectByPrimaryKey(userMoneyLog.getUserId());
            user.setUserMoney(new BigDecimal(user.getUserMoney()).add(userMoneyLog.getUseMoney()).longValue());
            userMapper.updateByPrimaryKey(user);
        }
        //记录时间
        userMoneyLog.setCreateTime(new Date());
        userMoneyLogMapper.insert(userMoneyLog);
        //5 记录订单余额使用日志
        return new Result( ShopCode.SHOP_SUCCESS.getSuccess(),ShopCode.SHOP_SUCCESS.getMessage());
    }
}
