package com.dhcc.shop.domain;

import java.io.Serializable;
import java.util.Date;

public class TradeGoodsNumberLog extends TradeGoodsNumberLogKey implements Serializable {
    private Integer goodsNumber;

    private Date logTime;

    private static final long serialVersionUID = 1L;

    public Integer getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(Integer goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", goodsNumber=").append(goodsNumber);
        sb.append(", logTime=").append(logTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}