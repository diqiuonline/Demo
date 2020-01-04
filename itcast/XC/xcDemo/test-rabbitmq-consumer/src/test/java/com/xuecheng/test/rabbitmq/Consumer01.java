package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * User: 李锦卓
 * Time: 2019/1/22 16:53
 */
public class Consumer01 {
    private static final String QUEYE = "helloworld";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        //设置rabbitmq所在服务器的ip和端口
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        //创建链接
        Connection connection = factory.newConnection();
        //创建通道
        Channel channel = connection.createChannel();

        /**
         * 声明队列 如果rabbitmq中没有此队列将自动创建
         * param1:队列名称
         * param2:是否持久化
         * param3:队列是否独占此链接
         * param4:队列不在使用时是否自动删除此队列
         * param5:队列参数
         */
        channel.queueDeclare(QUEYE, true, false, false, null);
        //定义消费方法
        DefaultConsumer consumer = new DefaultConsumer(channel){
            /**
             *消费者接受消息调用此方法
             * @param consumerTag 消费者的标签 在channel.basicConsume()去指定 可设置可不设置
             * @param envelope 消息包含的内容 可从中获取消息id，消息routingKey,交换机，消息和重传标志
             *                 （收到消息失败后是否需要重新发送）
             * @param properties  消息属性
             * @param body  消息内容
             * @throws IOException
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                //交换机
                String exchange = envelope.getExchange();
                //路由key
                String routingKey = envelope.getRoutingKey();
                //消息id 可用于消息已经接受
                long deliveryTag = envelope.getDeliveryTag();
                //消息内容
                String msg = new String(body, "utf-8");
                System.out.println("receive message.."+msg);
            }
        };
        /**
         * 监听队列string queue,boolean autoAck,Consumer callback
         * 参数明细
         * 1,队列名称
         * 2,是否自动回复，这只位true为表示消息接收到自动向mq回复接受到了，mq接受到回复会删除消息，设置为false
         * 则需要手动回复
         * 3，消费消息的方法，消费者接收到消息后调用此方法
         */
        channel.basicConsume(QUEYE, true, consumer);

    }
}