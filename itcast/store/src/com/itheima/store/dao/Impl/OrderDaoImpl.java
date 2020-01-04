package com.itheima.store.dao.Impl;

import com.itheima.store.dao.OrderDao;
import com.itheima.store.domain.Order;
import com.itheima.store.domain.OrderItem;
import com.itheima.store.domain.Product;
import com.itheima.store.utils.JDBCUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * User: 李锦卓
 * Time: 2018/7/12 11:42
 */
public class OrderDaoImpl implements OrderDao {
    //保存Order
    @Override
    public void saveOrder(Connection conn, Order order) throws SQLException {
        QueryRunner queryRunner = new QueryRunner();
        String sql = "insert into orders values (?,?,?,?,?,?,?,?) ";
        Object[] obj = {order.getOid(),order.getOrdertime(),order.getTotal()
                ,order.getState(),order.getAddress(),order.getName(),order.getTelephone(),order.getUser().getUid()};
        queryRunner.update(conn,sql,obj);
    }
    //保存OrderItem
    @Override
    public void saveOrderItem(Connection conn, OrderItem orderItem) throws SQLException {
        QueryRunner queryRunner = new QueryRunner();
        String sql = "insert into orderitem values (?,?,?,?,?)";
        Object[] obj = {orderItem.getItemid(),orderItem.getCount()
                ,orderItem.getSubtotal(),orderItem.getProduct().getPid(),orderItem.getOrder().getOid()};
        queryRunner.update(conn,sql,obj);
    }
    //查询该用户订单总数
    @Override
    public Long findCountByUid(String uid) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "select count(*) from orders where uid = ?";
        Long num = (Long) queryRunner.query(sql,new ScalarHandler(),uid);
        return num;
    }
    //查询该用户当前页订单集合
    @Override
    public List<Order> findByUid(String uid, Integer begin, Integer pageSize) throws Exception {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "select * from orders where uid = ? order by ordertime desc limit ?,?";
        List<Order> list = queryRunner.query(sql, new BeanListHandler<Order>(Order.class), uid,begin,pageSize);
        for (Order order:list){
            String sql2 = "SELECT * FROM product p,orderitem o WHERE p.pid = o.pid AND o.oid = ?";
            List<Map<String, Object>> list1 = queryRunner.query(sql2, new MapListHandler(), order.getOid());
            for (Map<String, Object> map:list1){
                Product product = new Product();
                BeanUtils.populate(product,map);
                OrderItem orderItem = new OrderItem();
                BeanUtils.populate(orderItem,map);
                orderItem.setProduct(product);
                order.getOrderItems().add(orderItem);
            }
        }
        return list;
    }
    //查询某一个订单
    @Override
    public Order findByOid(String oid) throws Exception {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "select * from orders where oid = ?";
        Order order = queryRunner.query(sql, new BeanHandler<Order>(Order.class),oid);
        String sql2 = "select * from product p,orderitem o where p.pid = o.pid and o.oid = ?";
        List<Map<String, Object>> list = queryRunner.query(sql2, new MapListHandler(), oid);
        for (Map<String,Object> map:list){
            Product product = new Product();
            BeanUtils.populate(product,map);
            OrderItem orderItem = new OrderItem();
            BeanUtils.populate(orderItem,map);
            orderItem.setProduct(product);
            order.getOrderItems().add(orderItem);
        }
        return order;
    }
    //修改订单
    @Override
    public void update(Order order) throws Exception {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "update orders set total = ?,state = ?,address = ?,name = ?,telephone = ? where oid = ?";
        Object[] obj = {order.getTotal(),order.getState(),order.getAddress(),order.getName(),order.getTelephone(),order.getOid()};
        queryRunner.update(sql,obj);
    }

}