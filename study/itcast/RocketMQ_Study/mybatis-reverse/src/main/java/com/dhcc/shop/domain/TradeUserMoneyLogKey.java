package com.dhcc.shop.domain;

import java.io.Serializable;

public class TradeUserMoneyLogKey implements Serializable {
    private Long userId;

    private Long orderId;

    private Integer moneyLogType;

    private static final long serialVersionUID = 1L;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getMoneyLogType() {
        return moneyLogType;
    }

    public void setMoneyLogType(Integer moneyLogType) {
        this.moneyLogType = moneyLogType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", orderId=").append(orderId);
        sb.append(", moneyLogType=").append(moneyLogType);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}