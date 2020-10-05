package com.dhcc.copywork.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* <p>
    * 
    * </p>
*
* @author author
* @since 2020-10-04
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class UserBill implements Serializable {

    private static final long serialVersionUID = 1L;

            /**
            * 主键
            */
            @TableId(value = "id", type = IdType.AUTO)
    private Long id;

            /**
            * 用户编号
            */
    private Long userId;

            /**
            * 账号
            */
    private String account;

            /**
            * 代理路径
            */
    private String userPaths;

            /**
            * 交易类型：TranTypes中值
            */
    private Integer tranType;

            /**
            * 交易金额,精确到分
            */
    private Double money;

            /**
            * 账号改变前的余额,精确到分
            */
    private Double balance;

            /**
            * 订单号，关联其他业务订单号
            */
    private String orderNo;

            /**
            * 添加时间
            */
    private LocalDateTime addTime;

            /**
            * 游戏编号
            */
    private Integer gameId;

            /**
            * 游戏编码
            */
    private String gameCode;

            /**
            * 期号
            */
    private String turnNum;

    private String cateCode;

            /**
            * 备注
            */
    private String remark;

            /**
            * 操作人
            */
    private String operateName;

            /**
            * 账变内容
            */
    private String content;

            /**
            * 分表ID
            */
    private Integer tableIndex;

    private Integer betpackId;


}
