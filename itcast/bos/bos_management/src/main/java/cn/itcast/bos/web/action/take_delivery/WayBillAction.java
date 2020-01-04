package cn.itcast.bos.web.action.take_delivery;

import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.service.take_delivery.OrderService;
import cn.itcast.bos.service.take_delivery.WayBillService;
import cn.itcast.bos.web.action.common.BaseAction;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * User: 李锦卓
 * Time: 2018/9/4 22:44
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class WayBillAction extends BaseAction<WayBill> {
    private static final Logger LOGGER = Logger.getLogger(WayBillAction.class);
    @Autowired
    private WayBillService wayBillService;
    @Autowired
    private OrderService orderService;
    //运单保存
    @Action(value = "waybill_save",results = {@Result(name = "success",type = "json")})
    public String waybillSave() throws IOException {
        Map<String,Object>result = new HashMap<String, Object>();
        try {
            //去除没有id的order对象
            if (model.getOrder() != null && (model.getOrder().getId() == null || model.getOrder().getId() == 0)) {
                model.setOrder(null);
            }
            String orderNum = model.getOrder().getOrderNum();
            Order order = orderService.find(orderNum);
            Integer id = order.getId();
            WayBill wayBill = wayBillService.findByOrderId(id);
            if (wayBill == null) {
                wayBillService.save(model);
                //保存成功
                result.put("success",true);
                result.put("msg", "保存运单成功");
                LOGGER.info("保存运单成功 运单号为："+model.getWayBillNum());
            } else {
                result.put("success",false);
                result.put("msg", "保存运单失败 订单已关联运单");
                LOGGER.error("保存运单失败 订单已关联运单");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //保存失败
            result.put("success",false);
            result.put("msg", "保存运单失败 运单已发出 不能修改");
            LOGGER.error("保存运单失败 运单号为："+model.getWayBillNum(),e);
        }
        ServletActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }
    private Integer rows;
    private Integer page;
    @Override
    public void setRows(Integer rows) {
        this.rows = rows;
    }
    @Override
    public void setPage(Integer page) {
        this.page = page;
    }
    //条件查询
    @Action(value = "waybill_pageQuery")
    public void pageQuery() throws IOException {
        //条件查询
        Pageable pageable = new PageRequest(page - 1, rows, new Sort(new Sort.Order(Sort.Direction.DESC, "id")));
        //调用业务层查询
        Page<WayBill> pageData = wayBillService.findPage(model,pageable);
       PagetoMapToJson(pageData);
    }
    //根据运单号查询指定运单
    @Action(value = "waybill_findByWayBillNum", results = {@Result(name = "success", type = "json",params={"excludeNullProperties","true"})})
    public String findByWayBillMNum() {
        WayBill wayBill = wayBillService.findOne(model.getWayBillNum());
        Map<String,Object>result = new HashMap<String, Object>();
        if (wayBill == null) {
            result.put("success", false);
        } else {
            result.put("success", true);
            result.put("WayBillData", wayBill);
        }
        ServletActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }

}