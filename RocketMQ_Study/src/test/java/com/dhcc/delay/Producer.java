package com.dhcc.delay;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
public class Producer {
    @Test
    public void demo() throws Exception {
        //创建消息生产者product，并指定生产者组名
        DefaultMQProducer producer = new DefaultMQProducer("group1");
        //指定nameserver地址
        producer.setNamesrvAddr("192.168.2.103:9876;192.168.2.107:9876");
        //启动producer
        producer.start();
        for (int i = 0; i < 10; i++) {
            //创建消息对象
            /**
             * 参数一：消息主题topic
             * 参数二：消息tag
             * 参数三：消息内容
             */
            Message msg = new Message("DelayTopic", "Tag1", ("Hello World" + i).getBytes());
            //设定延迟时间
            msg.setDelayTimeLevel(3);
            SendResult result = producer.send(msg);
            //发送状态
            SendStatus status = result.getSendStatus();
            System.out.println("发送结果" + result);
            //线程睡一秒
            TimeUnit.SECONDS.sleep(1);
        }
        //关闭生产者
        producer.shutdown();
    }
}
