package com.dhcc.base;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
public class base {
    /**
     * 发送同步消息
     * @throws Exception
     */
    @Test
    public void demo1() throws Exception {
        //实例化消息生产者
        DefaultMQProducer producer = new DefaultMQProducer("group1");
        //设置nameserver地址
        producer.setNamesrvAddr("192.168.2.103:9876;192.168.2.107:9876");
        //启动producer实例
        producer.start();
        for (int i = 0; i < 50; i++) {
            //创建消息 并指定topic Tag 和 消息体
            Message message = new Message("base", "TagA", ("Hello RocketMQ"
                    + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            //发送消息到一个broker
            SendResult sendResult = producer.send(message);
            //通过sendResult返回值查看消息是否发送成功
            System.out.println(sendResult);
        }
        //如果不在发送消息 关闭producter
        producer.shutdown();

    }

    /**
     * 发送异步消息
     */
    @Test
    public void demo2() throws Exception {
        //创建消息生产者producer 并指定生产者组名
        DefaultMQProducer producer = new DefaultMQProducer("group1");
        //指定nameserver地址
        producer.setNamesrvAddr("192.168.2.103:9876;192.168.2.107:9876");
        //启动produccer
        producer.start();
        for (int i = 0; i < 10; i++) {
            //创建消息对象指定消息topic tag 和消息体
            Message msg = new Message("base", "Tag2", ("Hello RocketMQ2" + i).getBytes());
            //发送异步消息
            producer.send(msg, new SendCallback() {
                /**
                 * 发送成功回调函数
                 * @param sendResult
                 */
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println("发送结果："+sendResult);
                }

                /**
                 *r 发送失败回调函数
                 * @param throwable
                 */
                @Override
                public void onException(Throwable throwable) {
                    System.out.println("发送异常："+throwable);
                }
            });
            TimeUnit.SECONDS.sleep(1);
        }
        //关闭
        producer.shutdown();
    }

    /**
     * 单向消息发送
     */
    @Test
    public void demo3() throws Exception {
        //创建消息的consumer 指定消息的组名字
        DefaultMQProducer producer = new DefaultMQProducer("group1");
        //指定nameserver的地址
        producer.setNamesrvAddr("192.168.2.103:9876;192.168.2.107:9876");
        //启动producter
        producer.start();
        for (int i = 0; i < 10; i++) {
            //创建消息对象
            Message msg = new Message("base", "Tag3", ("Hello RocketMQ" + i).getBytes());
            //发送单向消息
            producer.sendOneway(msg);
            TimeUnit.SECONDS.sleep(1);
        }
        //关闭
        producer.shutdown();
    }

    /**
     * 负载均衡消费 多个消费者共同消费队列消息 每个消费者处理的消息不同
     */
    @Test
    public void demo4() throws Exception {
        //1.创建消费者Consumer，制定消费者组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("group1");
        //2.指定Nameserver地址
        consumer.setNamesrvAddr("192.168.2.103:9876;192.168.2.107:9876");
        //3.订阅主题Topic和Tag
        consumer.subscribe("base", "*");

        //设定消费模式：负载均衡|广播模式
        consumer.setMessageModel(MessageModel.BROADCASTING);

        //4.设置回调函数，处理消息
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            //接受消息内容
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    System.out.println("consumeThread=" + Thread.currentThread().getName() + "," + new String(msg.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        //5.启动消费者consumer
        consumer.start();
    }
}
