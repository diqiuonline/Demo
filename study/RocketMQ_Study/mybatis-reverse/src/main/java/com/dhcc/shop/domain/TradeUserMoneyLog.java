package com.dhcc.shop.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TradeUserMoneyLog extends TradeUserMoneyLogKey implements Serializable {
    private BigDecimal useMoney;

    private Date createTime;

    private static final long serialVersionUID = 1L;

    public BigDecimal getUseMoney() {
        return useMoney;
    }

    public void setUseMoney(BigDecimal useMoney) {
        this.useMoney = useMoney;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", useMoney=").append(useMoney);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}