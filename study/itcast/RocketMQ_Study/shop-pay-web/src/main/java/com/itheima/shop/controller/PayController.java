package com.itheima.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.api.IPayService;
import com.itheima.entity.Result;
import com.itheima.shop.pojo.TradePay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/4/25 10:58
 */
@RequestMapping("/pay")
@RestController
public class PayController {
    @Reference
    private IPayService payService;
    @RequestMapping("/createPayment")
    public Result createPayment(@RequestBody TradePay tradePay) {
        return payService.createPayment(tradePay);
    }
    @RequestMapping("/callBackPayment")
    public Result callbackPayment(@RequestBody TradePay tradePay) {
        return payService.callbackPayment(tradePay);
    }
}
