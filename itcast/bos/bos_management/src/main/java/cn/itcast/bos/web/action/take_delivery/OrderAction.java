package cn.itcast.bos.web.action.take_delivery;

import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.service.take_delivery.OrderService;
import cn.itcast.bos.web.action.common.BaseAction;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

/**
 * User: 李锦卓
 * Time: 2018/9/5 10:53
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class OrderAction extends BaseAction<Order> {
    @Autowired
    private OrderService orderService;
    @Action(value = "order_findByOrderNum",results = {@Result(name = "success", type = "json",params={"excludeNullProperties","true"})})
    public String findByOrderNum() {
        Map<String,Object>result = new HashMap<String, Object>();
        Order order = orderService.find(model.getOrderNum());
        if (order == null) {
            result.put("success", false);
        } else {
            result.put("success",true);
            result.put("orderData",order);
            System.out.println(order);
        }
        ServletActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }
}