package com.roncoo.eshop.cache.model;

/**
 * @Author 李锦卓
 * @Description 商品信息
 * @Date 2022/7/9 18:52
 * @Version 1.0
 */
public class ProductInfo {
    private Long id;
    private String name;
    private Double price;

    public ProductInfo() {
    }

    public ProductInfo(Long id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
