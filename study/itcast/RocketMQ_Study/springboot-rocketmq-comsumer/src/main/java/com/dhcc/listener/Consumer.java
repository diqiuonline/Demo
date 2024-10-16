package com.dhcc.listener;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@RocketMQMessageListener(topic = "springboot-rocketmq", consumerGroup = "${rocketmq.consumer.group}")
@Component
public class Consumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String s) {
        System.out.println("接受到消息" + s);

    }
}
