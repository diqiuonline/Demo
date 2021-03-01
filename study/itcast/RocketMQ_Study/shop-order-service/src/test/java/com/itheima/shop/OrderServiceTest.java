package com.itheima.shop;

import com.itheima.api.IOrderService;
import com.itheima.entity.Result;
import com.itheima.shop.pojo.TradeOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/1/11 23:50
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderServiceTest {
    @Autowired
    private IOrderService orderService;

    @Test
    public void confirmOrder() throws Exception {
        long conponId = 34346546;
        long userId = 345963634385633280L;
        long goodsIs = 345959443973935104L;
        TradeOrder order = new TradeOrder();
        order.setGoodsId(goodsIs);
        order.setUserId(userId);
        order.setCouponId(conponId);
        order.setAddress("北京");
        order.setGoodsNumber(1);
        order.setGoodsPrice(new BigDecimal(1000));
        order.setOrderAmount(new BigDecimal(1000));
        order.setShippingFee(BigDecimal.ZERO );
        order.setMoneyPaid(new BigDecimal(100));
        order.setAddTime(new Date());
        Result result = orderService.confirmOrder(order);
        System.in.read();
    }
}