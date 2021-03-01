package com.dhcc.order;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class producer {
    @Test
    public void demo1() throws Exception {
        //创建消息的生产者producer 并指定生产者组名
        DefaultMQProducer producer = new DefaultMQProducer("group1");
        //指定nameserver地址
        producer.setNamesrvAddr("192.168.2.103:9876;192.168.2.107:9876");
        //启动producer
        producer.start();
        //构架消息集合
        List<OrderStep> orderSteps = OrderStep.buildOrders();
        for (int i = 0; i < orderSteps.size(); i++) {
            String body = orderSteps.get(i) + "";
            Message message = new Message("OrderTopic", "Order", "i" + i, body.getBytes());
            /**
             * 参数一：消息对象
             * 参数二：消息队列的选择器
             * 参数三：选择队列的业务表示
             */
            SendResult sendResult = producer.send(message, new MessageQueueSelector() {
                /**
                 * @param mqs 队列集合
                 * @param msg 消息对象
                 * @param arg 业务标识的参数
                 * @return
                 */
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    Long orderId = (Long) arg;
                    long index = orderId % mqs.size();
                    return mqs.get((int) index);
                }
            }, orderSteps.get(i).getOrderId());
            System.out.println("发送结果："+sendResult);

        }
        producer.shutdown();

    }

}
