package com.itheima.shop.mq;

import com.alibaba.fastjson.JSON;
import com.itheima.api.IUserService;
import com.itheima.constant.ShopCode;
import com.itheima.entity.MQEntity;
import com.itheima.shop.pojo.TradeUserMoneyLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;

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
    private IUserService iUserService;

    @Override
    public void onMessage(MessageExt messageExt) {
        try {
            //解析消息
            String body = new String(messageExt.getBody(), "UTF-8");
            log.info("接受消息成功");
            MQEntity mqEntity = JSON.parseObject(body, MQEntity.class);
            if (!StringUtils.isEmpty(mqEntity.getUserMoney()) && mqEntity.getUserMoney().compareTo(BigDecimal.ZERO) > 0) {
                TradeUserMoneyLog tradeUserMoneyLog = new TradeUserMoneyLog();
                tradeUserMoneyLog.setCreateTime(new Date());
                tradeUserMoneyLog.setUseMoney(mqEntity.getUserMoney());
                tradeUserMoneyLog.setOrderId(mqEntity.getOrderId());
                tradeUserMoneyLog.setUserId(mqEntity.getUserId());
                tradeUserMoneyLog.setMoneyLogType(ShopCode.SHOP_USER_MONEY_REFUND.getCode());
                iUserService.updateMoneyPaid(tradeUserMoneyLog);
                log.info("余额回退成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("余额回退失败");
        }

    }
}
