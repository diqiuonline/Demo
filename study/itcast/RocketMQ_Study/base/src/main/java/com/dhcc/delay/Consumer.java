package com.dhcc.delay;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

public class Consumer {
    public static void main(String[] args) throws Exception {
        //创建消息消费者 制定消费者组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("group1");
        //指定nameserver地址
        consumer.setNamesrvAddr("192.168.2.103:9876;192.168.2.107:9876");
        //订阅主题topic和tag
        consumer.subscribe("DelayTopic", "*");
        //设置回调函数，处理消息
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext
                    consumeConcurrentlyContext) {
                for (MessageExt msg : list) {
                    System.out.println("消息id+【"+msg.getMsgId()+"】，延迟时间："+(System.currentTimeMillis()-
                            msg.getStoreTimestamp()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        //启动消费者
        consumer.start();
        System.out.println("消费者启动");
    }
}
