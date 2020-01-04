package com.donghua.mq;

import com.donghua.utils.SmsUtils;
import org.springframework.stereotype.Service;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * User: 李锦卓
 * Time: 2018/11/14 14:36
 */
@Service
public class SmsConsumer implements MessageListener {
    public void onMessage(Message message) {
        MapMessage mapMessage = (MapMessage) message;
        //调用jms服务发送短信
        try {
           /* String result=  SmsUtils.sendSmsByHTTP(mapMessage.getString("telephone"),
                    mapMessage.getString("msg"));*/
            String result = "0003524";
            System.out.println(result);
            if (result.startsWith("000")) {
                //发送成功
                System.out.println("发送短信成功 手机号：" + mapMessage.getString("telephone")
                        + ";验证码：" + mapMessage.getString("msg"));
            } else {
                //发送失败
                throw new RuntimeException("短信发送失败，状态吗" + result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}