package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * User: 李锦卓
 * Time: 2019/1/21 21:33
 */
public class Producer01 {
    //队列名称
    private static final String QUEUE = "helloworld";

    public static void main(String[] args) throws Exception {
        //创建链接
        Connection connection = null;
        //创建通道
        Channel channel = null;
        try {
            //链接工厂
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            factory.setPort(5672);
            factory.setUsername("guest");
            factory.setPassword("guest");
            factory.setVirtualHost("/"); //rebbitmq 默认虚拟机名称为"/" 虚拟机相当于一个独立的mq服务器
            //创建与rabbitmq的tcp链接
            connection = factory.newConnection();
            //创建与Exchange的通道 每个链接可以创建多个通道 每个通道代表一个会话任务
            channel = connection.createChannel();
            /**
             * 声明队列 如果rabbitmq中没有此队列将自动创建
             * param1:队列名称
             * param2:是否持久化
             * param3:队列是否独占此链接
             * param4:队列不在使用时是否自动删除此队列
             * param5:队列参数
             */
            channel.queueDeclare(QUEUE, true, false, false, null);
            String message = "helloword小明" + System.currentTimeMillis();
            /**
             * 消息发布方法
             * param1:Exchange的名称，如果没有指定，则使用Default Exchange
             * param2:routingKey,消息路由的key 适用于Exchange（交换机）将消息发送到指定的消息队列
             * param3:消息包含的属性
             * param4:消息体
             */
            /**
             * 这里没有指定交换机，消息将发送给默认交换机，每个队列也会绑定那个默认的交换机，但是不能显示绑定或解除绑定
             * 默认的交换机 routingKey等于队列名称
             */
            channel.basicPublish("", QUEUE, null, message.getBytes());
            System.out.println("Send Message is:'"+message+"'");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (channel != null) {
                channel.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
}