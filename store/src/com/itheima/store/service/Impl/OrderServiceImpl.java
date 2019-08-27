package com.itheima.store.service.Impl;

import com.itheima.store.dao.OrderDao;
import com.itheima.store.domain.Order;
import com.itheima.store.domain.OrderItem;
import com.itheima.store.domain.PageBean;
import com.itheima.store.service.OrderService;
import com.itheima.store.utils.BeanFactory;
import com.itheima.store.utils.JDBCUtils;
import org.apache.commons.dbutils.DbUtils;

import javax.print.attribute.standard.Sides;
import java.security.Permission;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * User: 李锦卓
 * Time: 2018/7/12 11:41
 */
public class OrderServiceImpl implements OrderService {
    //保存Order和OrderItem
    @Override
    public void save(Order order) throws SQLException {
        Connection conn = null;
        try {
           //开启事务
            conn = JDBCUtils.getConnection();
            conn.setAutoCommit(false);
            //执行保存操作
            OrderDao orderDao = (OrderDao) BeanFactory.getBean("orderDao");
            orderDao.saveOrder(conn,order);
            //循环保存OrderItem
            for(OrderItem orderItem:order.getOrderItems()){
                orderDao.saveOrderItem(conn,orderItem);
            }
            //提交事务
            DbUtils.commitAndClose(conn);
        } catch (Exception e) {
            e.printStackTrace();
            //回滚事务
            DbUtils.rollbackAndClose(conn);
        }
    }
    //查询自己的订单 封装PageBean
    @Override
    public PageBean findByUid(Integer currpage, String uid) throws SQLException {
        try {
            PageBean pageBean = new PageBean();
            //封装当前页
            pageBean.setCurrPage(currpage);
            //封装总记录数
            OrderDao orderDao = (OrderDao) BeanFactory.getBean("orderDao");
            Long num = orderDao.findCountByUid(uid);
            pageBean.setTotalCount(num.intValue());
            //封装每页显示条数
            pageBean.setPageSize(5);
            //封装总页数
            Long num2 = num % pageBean.getPageSize()==0 ?  num % pageBean.getPageSize() :  num % pageBean.getPageSize()+1;
            pageBean.setTotoPage(num2.intValue());
            //封装当前页订单集合
            Integer begin = (currpage-1)*pageBean.getPageSize();
            List<Order>list = orderDao.findByUid(uid,begin,pageBean.getPageSize());
            pageBean.setList(list);
            return pageBean;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //查询某一个订单
    @Override
    public Order findByOid(String oid) throws Exception {
        OrderDao orderDao = (OrderDao) BeanFactory.getBean("orderDao");
        return orderDao.findByOid(oid);
    }
    //修改订单
    @Override
    public void update(Order order) throws Exception {
        OrderDao orderDao = (OrderDao) BeanFactory.getBean("orderDao");
        orderDao.update(order);
    }
}