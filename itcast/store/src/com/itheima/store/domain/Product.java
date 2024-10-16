package com.itheima.store.domain;

import java.util.Date;

/**
 * CREATE TABLE `product` (
 *   `pid` VARCHAR(32) NOT NULL,
 *   `pname` VARCHAR(50) DEFAULT NULL,
 *   `market_price` DOUBLE DEFAULT NULL,
 *   `shop_price` DOUBLE DEFAULT NULL,
 *   `pimage` VARCHAR(200) DEFAULT NULL,
 *   `pdate` DATE DEFAULT NULL,
 *   `is_hot` INT(11) DEFAULT NULL,
 *   `pdesc` VARCHAR(255) DEFAULT NULL,
 *   `pflag` INT(11) DEFAULT NULL,
 *   `cid` VARCHAR(32) DEFAULT NULL
 * ) ENGINE=INNODB DEFAULT CHARSET=utf8;
 */
public class Product  {
    private String pid;
    private String pname;
    private Double market_price;
    private Double shop_price;
    private String pimage;
    private Date pdate;
    private Integer is_hot;
    private String pdesc;
    private Integer pflag;
    private Category category;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public Double getMarket_price() {
        return market_price;
    }

    public void setMarket_price(Double market_price) {
        this.market_price = market_price;
    }

    public Double getShop_price() {
        return shop_price;
    }

    public void setShop_price(Double shop_price) {
        this.shop_price = shop_price;
    }

    public String getPimage() {
        return pimage;
    }

    public void setPimage(String pimage) {
        this.pimage = pimage;
    }

    public Date getPdate() {
        return pdate;
    }

    public void setPdate(Date pdate) {
        this.pdate = pdate;
    }

    public Integer getIs_hot() {
        return is_hot;
    }

    public void setIs_hot(Integer is_hot) {
        this.is_hot = is_hot;
    }

    public String getPdesc() {
        return pdesc;
    }

    public void setPdesc(String pdesc) {
        this.pdesc = pdesc;
    }

    public Integer getPflag() {
        return pflag;
    }

    public void setPflag(Integer pflag) {
        this.pflag = pflag;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}

