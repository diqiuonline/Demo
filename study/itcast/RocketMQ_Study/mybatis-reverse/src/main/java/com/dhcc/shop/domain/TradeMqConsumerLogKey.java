package com.dhcc.shop.domain;

import java.io.Serializable;

public class TradeMqConsumerLogKey implements Serializable {
    private String groupName;

    private String msgTag;

    private String msgKey;

    private static final long serialVersionUID = 1L;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
    }

    public String getMsgTag() {
        return msgTag;
    }

    public void setMsgTag(String msgTag) {
        this.msgTag = msgTag == null ? null : msgTag.trim();
    }

    public String getMsgKey() {
        return msgKey;
    }

    public void setMsgKey(String msgKey) {
        this.msgKey = msgKey == null ? null : msgKey.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", groupName=").append(groupName);
        sb.append(", msgTag=").append(msgTag);
        sb.append(", msgKey=").append(msgKey);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}