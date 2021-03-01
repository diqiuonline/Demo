package com.itheima.api;

import com.itheima.entity.Result;
import com.itheima.shop.pojo.TradePay;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/4/20 19:45
 */
public interface IPayService {
    public Result createPayment(TradePay tradePay);

    public Result callbackPayment(TradePay tradePay);
}
