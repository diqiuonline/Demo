package com.itheima.shop.mq;

import com.alibaba.fastjson.JSON;
import com.itheima.constant.ShopCode;
import com.itheima.entity.MQEntity;
import com.itheima.shop.mapper.TradeOrderMapper;
import com.itheima.shop.pojo.TradeCoupon;
import com.itheima.shop.pojo.TradeOrder;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/1/12 22:45
 */
@Component
@RocketMQMessageListener(topic = "${mq.order.topic}",
        consumerGroup = "${mq.order.consumer.group.name}", messageModel = MessageModel.BROADCASTING)
@Slf4j
public class CancelMQListener implements RocketMQListener<MessageExt> {

    @Autowired
    private TradeOrderMapper tradeOrderMapper;

    @Override
    public void onMessage(MessageExt messageExt) {
        try {
            //解析消息内容
            byte[] body = messageExt.getBody();
            String s = new String(body, "UTF-8");
            log.info("接收到消息");
            //查询订单消息
            MQEntity mqEntity = JSON.parseObject(s, MQEntity.class);
            TradeOrder tradeOrder = tradeOrderMapper.selectByPrimaryKey(mqEntity.getOrderId());
            //更新订单状态
            tradeOrder.setOrderStatus(ShopCode.SHOP_ORDER_CANCEL.getCode());
            tradeOrderMapper.updateByPrimaryKey(tradeOrder);
            log.info("订单状态设置为取消");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("订单取消失败");
        }
    }
}