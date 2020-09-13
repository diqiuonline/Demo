package com.dhcc.shanjupay.test.rocketmq.message;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/9/11 18:05
 */
@Component
@RocketMQMessageListener(topic = "TP_PAYMENT_RESULT",consumerGroup = "demo-consumer-group-test")
public class ConsumerSimpleTest implements RocketMQListener<String> {

    @Override
    public void onMessage(String s) {
        //此方法呗调用说明消息接受成功
        System.out.println(s);
    }
}
