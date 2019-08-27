package com.itheima.store.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: 李锦卓
 * Time: 2018/7/12 11:29
 */
public class Order {
    //订单ID
    private String oid;
    //订单时间
    private Date ordertime;
    //订单价格
    private Double total;
    //订单状态
    private Integer state;  //1未付款 2 已付款未发货 3 已发货未收货 4 确认收货 订单结束
    //收货地址
    private String address;
    //收货人
    private String name;
    //收货电话
    private String telephone;
    //用户
    private User user;
    //订单项集合
    private List<OrderItem>orderItems = new ArrayList<OrderItem>();

    public String getOid() {
        return oid;
    }
    public void setOid(String oid) {
        this.oid = oid;
    }
    public Date getOrdertime() {
        return ordertime;
    }
    public void setOrdertime(Date ordertime) {
        this.ordertime = ordertime;
    }
    public Double getTotal() {
        return total;
    }
    public void setTotal(Double total) {
        this.total = total;
    }
    public Integer getState() {
        return state;
    }
    public void setState(Integer state) {
        this.state = state;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}