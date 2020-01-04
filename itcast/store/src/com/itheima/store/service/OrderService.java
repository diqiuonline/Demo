package com.itheima.store.service;

import com.itheima.store.domain.Order;
import com.itheima.store.domain.PageBean;

import java.sql.SQLException;

public interface OrderService {
    //保存Order和OrderItem
    void save(Order order)throws SQLException;
    //查询自己的订单 封装PageBean
    PageBean findByUid(Integer currpage, String uid)throws SQLException;
    //查询某一个订单
    Order findByOid(String oid)throws Exception;
    //修改订单
    void update(Order order)throws Exception;
}
