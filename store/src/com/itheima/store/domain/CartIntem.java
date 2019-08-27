package com.itheima.store.domain;

/**
 * User: 李锦卓
 * Time: 2018/7/11 19:17
 */
public class CartIntem {
    //购物项中的商品
    private Product product;
    //商品购买的数量
    private Integer count;
    //小计
    private Double subtotal;

    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
    public Integer getCount() {
        return count;
    }
    public void setCount(Integer count) {
        this.count = count;
    }
    public Double getSubtotal() {
        return count * product.getShop_price();
    }
}