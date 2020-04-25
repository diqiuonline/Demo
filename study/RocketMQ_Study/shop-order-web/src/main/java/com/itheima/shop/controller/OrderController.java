package com.itheima.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.api.IOrderService;
import com.itheima.entity.Result;
import com.itheima.shop.pojo.TradeOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/4/25 10:50
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    private IOrderService orderService;

    @RequestMapping("/confirm")
    public Result confirmOrder(@RequestBody TradeOrder order) {
        return orderService.confirmOrder(order);
    }
}
