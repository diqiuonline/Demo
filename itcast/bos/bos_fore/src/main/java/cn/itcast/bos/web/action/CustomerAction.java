package cn.itcast.bos.web.action;

import cn.itcast.bos.constant.Constants;
import cn.itcast.bos.utils.MailUtils;
import cn.itcast.crm.domain.Customer;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.servlet.Servlet;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * User: 李锦卓
 * Time: 2018/8/28 20:51
 */
@ParentPackage("struts-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class CustomerAction extends BaseAction<Customer>{
    @Autowired
    @Qualifier("jmsQueueTemplate")
    private JmsTemplate jmsTemplate;

    @Action(value = "customer_sendSms")
    public String sendSms() throws Exception {
        //手机号保存在Customer对象
        //生成短信验证码
        String ranDomCoud = RandomStringUtils.randomNumeric(4);
        System.out.println(model.getTelephone());
        //将短信验证码保存到session中
        ServletActionContext.getRequest().getSession().setAttribute(model.getTelephone(), ranDomCoud);
        System.out.println("生成的验证码为:" + ranDomCoud);
        //编辑短信内容
        final String msg = "尊敬的用户你好 本次获得的验证码为" + ranDomCoud + ",服务电话：8008008800";
        //调用sms服务发送短信
        //String sms = SmsUtils.sendSmsByHTTP(model.getTelephone(), msg);
        //String sms = "000/xxxx";
        //调用mq服务 发送一条短信
        jmsTemplate.send("bos_sms", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("telephone", model.getTelephone());
                mapMessage.setString("msg",msg);
                return mapMessage;
            }
        });
        return NONE;
    }
    //属性驱动
    private String checkcode;

    public void setCheckcode(String checkcode) {
        this.checkcode = checkcode;
    }
    //注入
    @Autowired
    private RedisTemplate<String,String>  redisTemplate;

    @Action(value = "customer_regist", results = {
            @Result(name = "success", type = "redirect", location = "/signup-success.html"),
            @Result(name = "input", type = "redirect", location = "/signup.html")})
    public String regist() {
        //先校验短信 验证码如果不通过 直接返回注册页面
        //从session中获取先前生成的短信验证码
        String code = (String) ServletActionContext.getRequest().getSession().getAttribute(model.getTelephone());
        if (code == null || !code.equals(checkcode)) {
            return INPUT;
        }
        //调用WebService 连接crm  保存客户信息
        WebClient.create("http://localhost:9002/crm_management/services/customerService/customer").type(MediaType.APPLICATION_JSON).post(model);
        System.out.println("客户注册成功");
        //先发送一份激活邮件
        //生成激活码
        String activecode = RandomStringUtils.randomNumeric(32);
        //将激活吗保存到redis中 设置24小时有效期
        redisTemplate.opsForValue().set(model.getTelephone(), activecode, 24, TimeUnit.HOURS);
        //调用mailutils发送激活邮件
        String content = "尊敬的客户你好，请与24小时内激活 进行邮箱账户绑定 点击下面的地址完成绑定：<br/><a href = '"
                +MailUtils.activeUrl+"?telephone="+model.getTelephone()+"&activecode="+activecode+"'>邮箱绑定地址</a>";
        MailUtils.sendMail("激活邮件",content,model.getEmail());
        return SUCCESS;
    }
    //属性驱动
    private String activecode;

    public void setActivecode(String activecode) {
        this.activecode = activecode;
    }

    @Action(value = "customer_activeMail")
    public String activeMail() throws IOException {
        ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
        String activecodeRedis = redisTemplate.opsForValue().get(model.getTelephone());
        if (activecodeRedis == null || !activecodeRedis.equals(activecode)) {
            ServletActionContext.getResponse().getWriter().println(" 激活码无效 请重新登陆系统 绑定邮箱");
        }else {
            //激活码有效
            //防止重复绑定
            //调用crm webservic 查询客户信息 判断是否已经绑定
            Customer customer = WebClient.create("http://localhost:9002/crm_management/services/customerService/customer/telephone/"
                    + model.getTelephone()).accept(MediaType.APPLICATION_JSON).get(Customer.class);
            if (customer.getType() == null || customer.getType() != 1) {
                //没有绑定
                                WebClient.create("http://localhost:9002/crm_management/services/customerService/customer/updatetype/"
                        + model.getTelephone()).get();
                ServletActionContext.getResponse().getWriter().println("邮箱绑定成功");
            } else {
                //已经绑定成功
                ServletActionContext.getResponse().getWriter().println("邮箱已经绑定过 无需重复绑定");
            }
            //删除redis中的激活码
            //redisTemplate.delete(model.getTelephone());
        }
        return NONE;
    }
    //用户登陆
    @Action(value = "customer_login", results = {
            @Result(name = "success",location = "/index.html#/myhome",type = "redirect"),
            @Result(name = "login",location = "/login.html",type = "redirect")
    })
    public String login(){
        Customer customer = WebClient.create(Constants.CRM_MANAGEMENT_URL +
                "/services/customerService/customer/login?telephone="
                + model.getTelephone() + "&password=" + model.getPassword()).accept(MediaType.APPLICATION_JSON).get(Customer.class);
        System.out.println(123);
        if (customer != null) {
            ServletActionContext.getRequest().getSession().setAttribute("customer",customer);
            return SUCCESS;
        } else {
            return LOGIN;
        }
    }
}