package com.itheima.shop.mq;

import com.alibaba.fastjson.JSON;
import com.itheima.constant.ShopCode;
import com.itheima.exception.CastException;
import com.itheima.shop.mapper.TradeOrderMapper;
import com.itheima.shop.pojo.TradeOrder;
import com.itheima.shop.pojo.TradePay;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/4/24 22:35
 */
@Component
@Slf4j
@RocketMQMessageListener(topic = "${mq.pay.topic}", consumerGroup = "${mq.pay.consumer.group.name}",
        messageModel = MessageModel.BROADCASTING)
public class PaymentListener implements RocketMQListener<MessageExt> {
    @Autowired
    private TradeOrderMapper tradeOrderMapper;

    @Override
    public void onMessage(MessageExt messageExt) {
        log.info("接收到支付成功的消息");
        try {
            //解析消息内容
            TradePay tradePay = JSON.parseObject(new String(messageExt.getBody(), "utf-8"), TradePay.class);
            //更具订单id查询订单数据
            TradeOrder tradeOrder = tradeOrderMapper.selectByPrimaryKey(tradePay.getOrderId());
            if (StringUtils.isEmpty(tradeOrder)) {
                CastException.cast(ShopCode.SHOP_ORDER_INVALID);
            }
            //更改订单的支付状态为已支付
            tradeOrder.setOrderStatus(ShopCode.SHOP_ORDER_PAY_STATUS_IS_PAY.getCode());
            tradeOrder.setPayStatus(ShopCode.SHOP_PAYMENT_IS_PAID.getCode());
            //更新订单数据到数据库
            tradeOrderMapper.updateByPrimaryKey(tradeOrder);
            log.info("更改订单支付状态为已支付");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
