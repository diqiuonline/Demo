package com.dhcc.shanjupay.transaction.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@TableName("app_platform_channel")
@NoArgsConstructor                 //无参构造
@AllArgsConstructor                //有参构造
public class AppPlatformChannel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 应用id
     */
    @TableField("APP_ID")
    private String appId;

    /**
     * 平台支付渠道
     */
    @TableField("PLATFORM_CHANNEL")
    private String platformChannel;


}
