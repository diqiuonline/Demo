package cn.itcast.bos.web.action;

import cn.itcast.bos.constant.Constants;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.crm.domain.Customer;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.ws.rs.core.MediaType;

import static com.opensymphony.xwork2.Action.SUCCESS;

/**
 * User: 李锦卓
 * Time: 2018/9/3 20:07
 */
@ParentPackage("struts-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class OrderAction extends BaseAction<Order> {
    private String sendAreaInfo; //寄件人收件省市信息
    private String recAreaInfo; //收件人省份信息
    public void setSendAreaInfo(String sendAreaInfo) {
        this.sendAreaInfo = sendAreaInfo;
    }
    public void setRecAreaInfo(String recAreaInfo) {
        this.recAreaInfo = recAreaInfo;
    }
    @Action(value = "order_add", results = {@Result(name = "success", type = "redirect" ,location = "/index.html")})
    public String add() {
        //手动封装area关联
        Area sendArea = new Area();
        String[] sendAreaData = sendAreaInfo.split("/");
        sendArea.setProvince(sendAreaData[0]);
        sendArea.setCity(sendAreaData[1]);
        sendArea.setDistrict(sendAreaData[2]);
        Area recArea = new Area();
        String[] recAreaDate = recAreaInfo.split("/");
        recArea.setProvince(recAreaDate[0]);
        recArea.setCity(recAreaDate[1]);
        recArea.setDistrict(sendAreaData[2]);
        //保存到数据库
        model.setRecArea(recArea);
        model.setSendArea(sendArea);
        //关联客户
        Customer customer = (Customer) ServletActionContext.getRequest().getSession().getAttribute("customer");
        model.setCustomer_id(customer.getId());
        //调用webservice 将数据传到bos_management 系统
        WebClient.create(Constants.BOS_MANAGEMENT_URL+"/services/orderServices/order").type(MediaType.APPLICATION_JSON).post(model);
        return SUCCESS;
    }
}