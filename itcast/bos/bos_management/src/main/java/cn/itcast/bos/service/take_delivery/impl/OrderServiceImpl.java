package cn.itcast.bos.service.take_delivery.impl;

import cn.itcast.bos.constant.Constants;
import cn.itcast.bos.dao.base.AreaRepository;
import cn.itcast.bos.dao.base.FixedAreaRepository;
import cn.itcast.bos.dao.take_delivery.OrderRepository;
import cn.itcast.bos.dao.take_delivery.WorkBillRepository;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.domain.take_delivery.WorkBill;
import cn.itcast.bos.service.take_delivery.OrderService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

/**
 * User: 李锦卓
 * Time: 2018/9/3 21:24
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private FixedAreaRepository fixedAreaRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private AreaRepository areaRepository;
    @Autowired
    private WorkBillRepository workBillRepository;
    @Autowired
    @Qualifier("jmsQueueTemplate")
    private JmsTemplate jmsTemplate;
    //保存订单
    @Override
    public void saveOrder(Order order) {
        System.out.println(order);
        System.out.println(order.getSendAddress());
        //设置订单号
        order.setOrderNum(UUID.randomUUID().toString());
        //设置下单时间
        order.setOrderTime(new Date());
        //设置订单状态
        order.setStatus("1"); //带取件

        //寄件人省市区
        Area area = order.getSendArea();
        Area persistArea = areaRepository.findByProvinceAndCityAndDistrict(area.getProvince(),area.getCity(),area.getDistrict());
        // 收件人 省市区
        Area recArea = order.getSendArea();
        Area persistRecArea = areaRepository.findByProvinceAndCityAndDistrict(recArea.getProvince(), recArea.getCity(), recArea.getDistrict());
        order.setSendArea(persistArea);
        order.setRecArea(persistRecArea);

        //查询定区编号
        String fixedAreaId = WebClient.create(Constants.CRM_MANAGEMENT_URL + "/services/customerService/customer/findFixedAreaIdByAddress?" +
                "address=" + order.getSendAddress()).accept(MediaType.APPLICATION_JSON).get(String.class);
        System.out.println(fixedAreaId);
        if (fixedAreaId != null) {
            //客户输入地址和预留地址相同 找到快递员
            FixedArea fixedArea = fixedAreaRepository.findOne(fixedAreaId);
            Courier courier = fixedArea.getCouriers().iterator().next();
            if (courier != null) {
                //找到快递员
                //将订单和快递员关联  多方维护外键
                saveOrder(order,courier);
                return;
            }
        }
        //自动分单逻辑  通过省市区 查询关键字分区
        for (SubArea subArea : persistArea.getSubareas()) {
            //当前客户下单地址关键字是否包含分区
            if (order.getSendAddress().contains(subArea.getKeyWords())) {
                //找到分区 找到定区 找到快递员
                Iterator<Courier> iterator = subArea.getFixedArea().getCouriers().iterator();
                if (iterator.hasNext()) {
                    Courier courier = iterator.next();
                    if (courier != null) {
                        //自动分单成功
                        saveOrder(order,courier);
                        return;
                    }
                }
            }
        }
        for (SubArea subArea : persistArea.getSubareas()) {
            //当前下单地址是否包含分区辅助关键字
            if (order.getSendAddress().contains(subArea.getAssistKeyWords())) {
                //找到分区 找到定区 找到快递员
                Iterator<Courier> iterator = subArea.getFixedArea().getCouriers().iterator();
                if (iterator.hasNext()) {
                    Courier courier = iterator.next();
                    if (courier != null) {
                        //自动分单成功
                        saveOrder(order,courier);
                        return;
                    }
                }
            }
        }
        //自动分单失败 进入人工调度
        order.setOrderType("2");
        orderRepository.save(order);
    }
    //生成工单 发送短信
    private void generateWorkBill(final Order order) {
        //生成工单
        WorkBill workBill = new WorkBill();
        workBill.setType("新");
        workBill.setPickstate("新单");
        workBill.setBuildtime(new Date());
        workBill.setRemark(order.getRemark());
        String smsNumber = RandomStringUtils.randomNumeric(4);
        workBill.setSmsNumber(smsNumber);  //短信序号
        workBill.setOrder(order);
        workBill.setCourier(order.getCourier());
        workBillRepository.save(workBill);
        //发送短信  调用mq服务 发送一条短信
        final String msg = "短信序号"+smsNumber+",取件地址:"+order.getSendAddress()+",联系人:"
                +order.getSendName()+",手机："+order.getSendMobile()+
                ",快递员悄悄话："+order.getSendMobileMsg();
        jmsTemplate.send("bos_sms", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("telephone", order.getCourier().getTelephone());
                mapMessage.setString("msg",msg);
                return mapMessage;
            }
        });
        //修改工单状态
        workBill.setPickstate("已通知");
    }
    private void saveOrder(Order order, Courier courier){
        order.setCourier(courier);
        //设置人工分单
        order.setOrderType("1");
        orderRepository.save(order);
        generateWorkBill(order);
    }
    //查询订单
    @Override
    public Order find(String orderNum) {
        return orderRepository.findByOrderNum(orderNum);
    }
}