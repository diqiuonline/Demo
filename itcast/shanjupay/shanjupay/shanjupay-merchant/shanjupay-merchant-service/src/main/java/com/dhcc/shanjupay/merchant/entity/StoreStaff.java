package com.dhcc.shanjupay.merchant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("store_staff")
public class StoreStaff implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 门店标识
     */
    @TableField("STORE_ID")
    private Long storeId;

    /**
     * 员工标识
     */
    @TableField("STAFF_ID")
    private Long staffId;


}
