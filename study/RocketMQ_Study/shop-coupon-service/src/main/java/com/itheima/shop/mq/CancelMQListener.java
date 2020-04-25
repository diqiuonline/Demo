package com.itheima.shop.mq;

import com.alibaba.fastjson.JSON;
import com.itheima.constant.ShopCode;
import com.itheima.entity.MQEntity;
import com.itheima.shop.mapper.TradeCouponMapper;
import com.itheima.shop.pojo.TradeCoupon;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/1/12 17:26
 */
@Component
@RocketMQMessageListener(topic = "${mq.order.topic}",
        consumerGroup = "${mq.order.consumer.group.name}", messageModel = MessageModel.BROADCASTING)
@Slf4j
public class CancelMQListener implements RocketMQListener<MessageExt> {
    @Autowired
    private TradeCouponMapper tradeCouponMapper;

    @Override
    public void onMessage(MessageExt messageExt) {

        try {
            //解析消息内容
            byte[] body = messageExt.getBody();
            String s = new String(body, "UTF-8");
            log.info("接收到消息");
            //查询优惠卷信息
            MQEntity mqEntity = JSON.parseObject(s, MQEntity.class);
            if (!StringUtils.isEmpty(mqEntity.getCouponId())) {
                TradeCoupon tradeCoupon = tradeCouponMapper.selectByPrimaryKey(mqEntity.getCouponId());
                tradeCoupon.setUsedTime(null);
                tradeCoupon.setOrderId(null);
                tradeCoupon.setIsUsed(ShopCode.SHOP_COUPON_UNUSED.getCode());
                //更改优惠卷状态
                tradeCouponMapper.updateByPrimaryKey(tradeCoupon);
                log.info("回退优惠卷成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("回退优惠卷失败");
        }



    }
}
