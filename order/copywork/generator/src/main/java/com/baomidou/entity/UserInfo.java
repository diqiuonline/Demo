package com.baomidou.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
* @since 2020-08-21
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

            /**
            * 主键
            */
            @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

            /**
            * 登陆账号
            */
    private String account;

            /**
            * 登陆密码
            */
    private String password;

            /**
            * 真实姓名
            */
    private String fullName;

            /**
            * 昵称
            */
    private String nickname;

            /**
            * 手机号
            */
    private String phone;

            /**
            * 邮箱
            */
    private String email;

            /**
            * 所属代理ID
            */
    private Long superId;

            /**
            * 所属代理名称
            */
    private String superName;

            /**
            * 所属代理路径
            */
    private String superPath;

            /**
            * 用户状态（0：待审核；1：正常；2：停用）
            */
    private Integer state;

            /**
            * 限制投注:0正常,1限制
            */
    private Integer limitBet;

            /**
            * 注册方式:1-后台添加；2-推广注册; 3-网页注册; 4-APP注册
            */
    private Integer regWay;

            /**
            * 注册ip
            */
    private String regIp;

            /**
            * 注册时间
            */
    private LocalDateTime addTime;

            /**
            * 更新时间
            */
    private LocalDateTime updateTime;

            /**
            * 校验字段
            */
    private String fk;

            /**
            * 代理级别
            */
    private Integer dlLevel;

            /**
            * 是否为代理0否,1是
            */
        @TableField("is_dl")
    private Boolean dl;

            /**
            * 会员层级
            */
    private String hyLevel;

            /**
            * qq
            */
    private String qq;

            /**
            * 微信
            */
    private String weixin;

    private Integer sex;

            /**
            * 推广码(对应intr_info表)
            */
    private String intrCode;

            /**
            * 会员类型HY会员TEST试玩会员DL代理
            */
    private String type;

            /**
            * 1电话已验证,2表示允许接收短信,4表示允许接收邮件(值可以进行或运算)
            */
    private Integer receive;

            /**
            * 推荐码(会员介绍会员)
            */
    private String recommendCode;

            /**
            * 生日(格式0706)
            */
    private String birthday;

            /**
            * 地址
            */
    private String address;

            /**
            * 是否完善资料0表示否1表示是3已发送完善资料彩金
            */
    private Integer perfectInfo;

            /**
            * 0正常,1密码错误限制，此字段没用，用state来控制
            */
    private Integer limited;

            /**
            * 浏览器(注册时获取)
            */
    private String browser;

            /**
            * 操作系统(注册时获取)
            */
    private String operatingSystem;

            /**
            * 请求的完整UserAgent
            */
    private String userAgent;

            /**
            * 分表对应ID
            */
    private Integer tableIndex;

            /**
            * 返水百分比
            */
    private Double rebate;

            /**
            * 微信用户标识唯一码
            */
    private String wxUnionid;

            /**
            * 用户备注
            */
    private String userMemo;

            /**
            * 用户颜色
            */
    private String userColor;

            /**
            * 是否给日工资0否,1是
            */
    private Integer isDayWage;

            /**
            * 是否限制银行入款0否,1是
            */
    private Integer limitRech;

    private String remark;


}
