package com.dhcc.shop.domain;

import java.io.Serializable;
import java.util.Date;

public class TradeMqConsumerLog extends TradeMqConsumerLogKey implements Serializable {
    private String msgId;

    private String msgBody;

    private Integer consumerStatus;

    private Integer consumerTimes;

    private Date consumerTimestamp;

    private String remark;

    private static final long serialVersionUID = 1L;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId == null ? null : msgId.trim();
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody == null ? null : msgBody.trim();
    }

    public Integer getConsumerStatus() {
        return consumerStatus;
    }

    public void setConsumerStatus(Integer consumerStatus) {
        this.consumerStatus = consumerStatus;
    }

    public Integer getConsumerTimes() {
        return consumerTimes;
    }

    public void setConsumerTimes(Integer consumerTimes) {
        this.consumerTimes = consumerTimes;
    }

    public Date getConsumerTimestamp() {
        return consumerTimestamp;
    }

    public void setConsumerTimestamp(Date consumerTimestamp) {
        this.consumerTimestamp = consumerTimestamp;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", msgId=").append(msgId);
        sb.append(", msgBody=").append(msgBody);
        sb.append(", consumerStatus=").append(consumerStatus);
        sb.append(", consumerTimes=").append(consumerTimes);
        sb.append(", consumerTimestamp=").append(consumerTimestamp);
        sb.append(", remark=").append(remark);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}