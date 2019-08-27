package com.itheima.store.domain;

import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * User: 李锦卓
 * Time: 2018/7/11 19:23
 * 购物车
 */
public class Cart {
    //总计
    private Double total = 0d;
    //map总key是商品的pid value是购物项
    private Map<String,CartIntem> map = new LinkedHashMap<String, CartIntem>();

    public Double getTotal() {
        return total;
    }

    public Map<String, CartIntem> getMap() {
        return map;
    }
    //将购物项添加到购物车
    public void addCart(CartIntem cartIntem){
        //获取购物项中购买的商品ID
        String pid = cartIntem.getProduct().getPid();
        //判断之前是否买过
        if(!map.containsKey(pid)){
            //没有购买过
            map.put(pid,cartIntem);
        }else {
            //之前购买过商品
            CartIntem oldcartIntem1 = map.get(pid);
            oldcartIntem1.setCount(oldcartIntem1.getCount()+cartIntem.getCount());
        }
        total += cartIntem.getSubtotal();
    }
    //清除购物项
    public void clear(){
        map.clear();
        total = 0d;
    }
    //将购物项从购物车移除
    public void removeCart(String pid){
        CartIntem cartIntem = map.remove(pid);
        total -= cartIntem.getSubtotal();
    }
}