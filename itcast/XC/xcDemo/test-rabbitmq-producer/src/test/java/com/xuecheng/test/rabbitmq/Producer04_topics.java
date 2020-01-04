package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * User: 李锦卓
 * Time: 2019/2/16 14:39
 */
public class Producer04_topics {
    //队列名称
    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    private static final String EXCHANGE_TOPICS_INFORM = "exchange_topics_inform";
    private static final String ROUTINGKEY_EMAIL="inform.#.email.#";
    private static final String ROUTINGKEY_SMS="inform.#.sms.#";

    public static void main(String[] args) {
        //创建链接
        Connection connection = null;
        //连接通道
        Channel channel = null;
        try {
            //链接工厂
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("127.0.0.1");
            factory.setPort(5672);
            factory.setUsername("guest");
            factory.setPassword("guest");
            factory.setVirtualHost("/"); //rabbitmq的默认虚拟机名称为 "/" 虚拟机相当于一个独立的mq服务器
            //创建一个链接
            connection = factory.newConnection();
            //创建与交换机的通道 每个通道代表一个会话
            channel = connection.createChannel();
            //声明交换机String exchange,BuiltinExchangeType type
            /**
             * 参数明细
             * 1 交换机名称
             * 2 交换机类型
             * fanout 发布订阅publish/subscribe
             * topic 通配符模式
             * direct 路由模式 Routing
             * headers 对应headers模式
             */
            channel.exchangeDeclare(EXCHANGE_TOPICS_INFORM, BuiltinExchangeType.TOPIC);
            //声明队列
            /**
             * 参数明细
             * 1 队列名称
             * 2 是否持久化
             * 3 是否独占此队列
             * 4 队列不用是否自动删除
             * 5 参数
             */
            channel.queueDeclare(QUEUE_INFORM_EMAIL, true, false, false, null);
            channel.queueDeclare(QUEUE_INFORM_SMS, true, false, false, null);
          //交换机和队列绑定
            /**
             * 参数明细：
             * 1、queue 队列名称
             * 2、exchange 交换机名称
             * 3、routingKey 路由key，作用是交换机根据路由key的值将消息转发到指定的队列中，在发布订阅模式中调协为空字符串
             */
            channel.queueBind(QUEUE_INFORM_EMAIL, EXCHANGE_TOPICS_INFORM, ROUTINGKEY_EMAIL);
            channel.queueBind(QUEUE_INFORM_SMS, EXCHANGE_TOPICS_INFORM, ROUTINGKEY_SMS);
            //发送消息
            for (int i = 0; i < 10; i++) {
                String message = "email inform to user "+i;
                //向交换机发送消息
                /**
                 * 参数明细
                 * 1 交换机名称 不指定默认使用默认交换机Default Exchange
                 * 2 routingKey（路由key） 更具key名称将消息转发到消息队列 这里填写队列名称表示消息将发送到此队列
                 * 3 消息属性
                 * 4 消息内容
                 */
                channel.basicPublish(EXCHANGE_TOPICS_INFORM,"inform.email",null,message.getBytes());
                System.out.println("Send Message is :'"+message+"'");
            }
            for (int i = 0; i < 5; i++) {
                String message = "sms inform to user "+i;
                //向交换机发送消息
                /**
                 * 参数明细
                 * 1 交换机名称 不指定默认使用默认交换机Default Exchange
                 * 2 routingKey（路由key） 更具key名称将消息转发到消息队列 这里填写队列名称表示消息将发送到此队列
                 * 3 消息属性
                 * 4 消息内容
                 */
                channel.basicPublish(EXCHANGE_TOPICS_INFORM,"inform.sms",null,message.getBytes());
                System.out.println("Send Message is :'"+message+"'");
            }
            for (int i = 0; i < 5; i++) {
                String message = "sms and email inform to user "+i;
                //向交换机发送消息
                /**
                 * 参数明细
                 * 1 交换机名称 不指定默认使用默认交换机Default Exchange
                 * 2 routingKey（路由key） 更具key名称将消息转发到消息队列 这里填写队列名称表示消息将发送到此队列
                 * 3 消息属性
                 * 4 消息内容
                 */
                channel.basicPublish(EXCHANGE_TOPICS_INFORM,"inform.sms.email",null,message.getBytes());
                System.out.println("Send Message is :'"+message+"'");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}