package cn.itcast.bos.service.take_delivery;

import cn.itcast.bos.domain.take_delivery.Order;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

public interface OrderService {
    //保存订单
    @Path("/order")
    @POST
    @Consumes({"application/xml","application/json"})
    void saveOrder(Order order);
    //查询订单
    Order find(String orderNum);
}
