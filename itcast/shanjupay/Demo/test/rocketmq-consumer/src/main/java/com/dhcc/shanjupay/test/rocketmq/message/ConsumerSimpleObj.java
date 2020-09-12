package com.dhcc.shanjupay.test.rocketmq.message;

import com.dhcc.shanjupay.test.rocketmq.model.OrderExt;
import org.apache.rocketmq.client.consumer.listener.MessageListener;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/9/11 18:05
 */
@Component
@RocketMQMessageListener(topic = "my-topic-obj",consumerGroup = "demo-consumer-group-obj")
public class ConsumerSimpleObj implements RocketMQListener<MessageExt> {

    @Override
    public void onMessage(MessageExt s) {
        System.out.println(new String(s.getBody()));
        int reconsumeTimes = s.getReconsumeTimes();
        if (reconsumeTimes > 2) {
            System.out.println("rocketmq不处理了  。。。。");
            s.setReconsumeTimes(16);
        }
       /* if (true) {
            throw new RuntimeException("处理消息失败");
        }*/
        //此方法呗调用说明消息接受成功
        //System.out.println(s);
    }
}


class MessageListenerImpl implements MessageListener {

}