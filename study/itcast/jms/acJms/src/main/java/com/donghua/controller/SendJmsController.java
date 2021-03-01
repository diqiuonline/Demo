package com.donghua.controller;

import com.donghua.canstant.Canstant;
import com.donghua.mapper.UserMapper;
import com.donghua.pojo.User;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;

/**
 * User: 李锦卓
 * Time: 2018/11/14 11:31
 */
@RestController
public class SendJmsController {
    @Autowired
    @Qualifier("jmsQueueTemplate")
    private JmsTemplate jmsTemplate;

    @RequestMapping("/sendJms")
    public String sendJms(final String telephone, HttpSession httpSession) throws Exception {
        //生成短信验证码
        String randomCode = RandomStringUtils.randomNumeric(4);
        System.out.println(randomCode);
        //将短信验证码存储到session中
        httpSession.setAttribute("randomCode", randomCode);
        //编辑短信内容
        final String msg = "尊敬的用户您好，本次获得的验证码为：" + randomCode;
        //调用mq服务 发送一条消息
        jmsTemplate.send("acJms", new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("telephone", telephone);
                mapMessage.setString("msg", msg);
                return mapMessage;
            }
        });
        //int i = 1/0;
        return "NONE";
    }
    /*@Autowired
    private UserMapper userMapper;*/

    @RequestMapping("/regist")
    public ModelAndView regist(User user, String randomCode, HttpSession httpSession, HttpServletResponse response) {
        Object randomCode1 = httpSession.getAttribute("randomCode");
        if (!randomCode.equals(randomCode1)) {
            ModelAndView mv = new ModelAndView("error");
            return mv;
        } else {
          //调用webservice服务 完成注册
            WebClient.create(Canstant.WEBSERVICE_URL+Canstant.WEBSERVICE_CONTEXT+"/services/userService/user")
                    .type(MediaType.APPLICATION_JSON).post(user);
            ModelAndView mv = new ModelAndView("redirect:login.jsp");
            return mv;
        }

    }
}