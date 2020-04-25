package com.itheima.shop;


import com.itheima.constant.ShopCode;
import com.itheima.shop.pojo.TradePay;
import com.itheima.shop.service.impl.PayServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/4/24 22:52
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class PayServiceTest {
    @Autowired
    private PayServiceImpl payService;
    @Test
    public void createPayment() {
        TradePay tradePay = new TradePay();
        tradePay.setOrderId(449626571003793408L);
        tradePay.setPayAmount(new BigDecimal(880));
        payService.createPayment(tradePay);
    }
    @Test
    public void collbackPayment() throws Exception {
        TradePay tradePay = new TradePay();
        tradePay.setOrderId(449626571003793408L);
        tradePay.setPayId(451202393032499200L);
        tradePay.setIsPaid(ShopCode.SHOP_ORDER_PAY_STATUS_IS_PAY.getCode());
        payService.callbackPayment(tradePay);
        System.in.read();
    }
}
