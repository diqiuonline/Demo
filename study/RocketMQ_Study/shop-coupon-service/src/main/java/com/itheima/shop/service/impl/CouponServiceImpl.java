package com.itheima.shop.service.impl;

import com.itheima.api.ICouponService;
import com.itheima.constant.ShopCode;
import com.itheima.entity.Result;
import com.itheima.exception.CastException;
import com.itheima.shop.mapper.TradeCouponMapper;
import com.itheima.shop.pojo.TradeCoupon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/1/5 23:27
 */
public class CouponServiceImpl implements ICouponService {
    @Autowired
    private TradeCouponMapper couponMapper;
    @Override
    public TradeCoupon findOne(long couponId) {
        if (StringUtils.isEmpty(couponId)) {
            CastException.cast(ShopCode.SHOP_REQUEST_PARAMETER_VALID);
        }
        return couponMapper.selectByPrimaryKey(couponId);
    }

    //更新优惠卷
    @Override
    public Result updateCouponStatus(TradeCoupon coupon) {
        if (StringUtils.isEmpty(coupon) || StringUtils.isEmpty(coupon.getCouponId())) {
            CastException.cast(ShopCode.SHOP_REQUEST_PARAMETER_VALID);
        }
        couponMapper.updateByPrimaryKey(coupon);
        return new Result(ShopCode.SHOP_SUCCESS.getSuccess(),ShopCode.SHOP_SUCCESS.getMessage());
    }
}
