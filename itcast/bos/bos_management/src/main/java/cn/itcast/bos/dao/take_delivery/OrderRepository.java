package cn.itcast.bos.dao.take_delivery;

import cn.itcast.bos.domain.take_delivery.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    //查询订单
    Order findByOrderNum(String orderNum);
}
