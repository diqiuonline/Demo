package com.itheima.store.dao;

import com.itheima.store.domain.Order;
import com.itheima.store.domain.OrderItem;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface OrderDao {
    //保存order
    void saveOrder(Connection conn, Order order)throws SQLException;
    //保存orderItem
    void saveOrderItem(Connection conn, OrderItem orderItem)throws SQLException;
    //查询该用户订单总数
    Long findCountByUid(String uid)throws SQLException;
    //查询该用户当前页订单集合
    List<Order> findByUid(String uid, Integer begin, Integer pageSize)throws Exception;
    //查询某一个订单
    Order findByOid(String oid)throws  Exception;
    //修改订单
    void update(Order order)throws  Exception;
}
