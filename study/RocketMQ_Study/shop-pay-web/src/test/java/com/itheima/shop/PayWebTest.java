package com.itheima.shop;

import com.itheima.constant.ShopCode;
import com.itheima.entity.Result;
import com.itheima.shop.pojo.TradePay;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/4/25 17:49
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class PayWebTest {
    @Autowired
    private RestTemplate template;
    @Value("${shop.pay.baseURI}")
    private String baseURI;
    @Value("${shop.pay.createPayment}")
    private String createPaymentPath;
    @Value("${shop.pay.callbackPayment}")
    private String callbackPaymentPath;

    @Test
    public void createPayment() {
        TradePay tradePay = new TradePay();
        tradePay.setOrderId(451481317662658560L);
        tradePay.setPayAmount(new BigDecimal(880));
        Result body = template.postForEntity(baseURI + createPaymentPath, tradePay, Result.class).getBody();
        System.out.println(body);
    }
    @Test
    public void CallbackPaymentPath() {
        TradePay tradePay = new TradePay();
        tradePay.setOrderId(451481317662658560L);
        tradePay.setPayId(451484648523046912L);
        tradePay.setIsPaid(ShopCode.SHOP_ORDER_PAY_STATUS_IS_PAY.getCode());
        Result result = template.postForEntity(baseURI + callbackPaymentPath, tradePay, Result.class).getBody();
        System.out.println(result);
    }

}
