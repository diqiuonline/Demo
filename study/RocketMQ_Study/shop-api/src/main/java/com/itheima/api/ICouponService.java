package com.itheima.api;

import com.itheima.entity.Result;
import com.itheima.shop.pojo.TradeCoupon;

/**
 * 优惠卷服务
 */
public interface ICouponService {
    //根据id查询优惠卷
    public TradeCoupon findOne(long couponId);

    //更新优惠卷状态
    Result updateCouponStatus(TradeCoupon coupon);
}
