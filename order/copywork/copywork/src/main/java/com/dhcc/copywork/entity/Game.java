package com.dhcc.copywork.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
* <p>
    * 
    * </p>
*
* @author author
* @since 2020-07-22
*/
    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @TableName("")
    public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

            /**
            * 主键
            */
            @TableId(value = "id", type = IdType.ID_WORKER)
    private Integer id;

            /**
            * 游戏名称
            */
            @TableField("`name`")
    private String name;

            /**
            * 排序
            */
    private Integer sort;

            /**
            * 游戏类型
            */
    private String type;

            /**
            * 分类:1-彩票;2-体育;3-真人
            */
    private Integer cate;

            /**
            * 每期最大奖金
            */
    private Long maxReward;

            /**
            * 游戏开关,0-开;1-关
            */
            @TableField("`open`")
    private Integer open;

            /**
            * 游戏公休开始时间
            */
    private Date restStartDate;

            /**
            * 游戏公休结束时间
            */
    private Date restEndDate;

            /**
            * 期号格式
            */
    private String turnFormat;

            /**
            * 当前期号
            */
    private String curTurnNum;

            /**
            * 每天期数
            */
    private Integer amount;

            /**
            * 是否封盘; 0-正常、1-封盘
            */
    private Integer isBan;

            /**
            * 编码
            */
            @TableField("`code`")
    private String code;

            /**
            * 规则：timeFormat 时间格式,additive累加格式
            */
    private String rules;

            /**
            * 是否开启官方玩法0否,1是
            */
    private Integer isOffcial;

            /**
            * 是否开启信用玩法0否,1是
            */
    private Integer isCredit;

            /**
            * 开奖结果格式
            */
    private String openNumFormat;

            /**
            * 开奖号码个数
            */
    private Integer openNum;

            /**
            * 开奖号码长度
            */
    private Integer openLength;

            /**
            * 开奖频率
            */
    private String openFrequency;

            /**
            * 期号位数例如 01 位2位
            */
    private Integer turnLength;

            /**
            * 是否热门游戏0否1是
            */
    private Integer hot;

            /**
            * 备注说明
            */
    private String remark;

            /**
            * 采集类型0默认API,1手动
            */
    private Integer collectType;

            /**
            * 极速类型0否,1极速
            */
    private Integer jsType;

            /**
            * 时间间隔分钟
            */
            @TableField("`interval`")
    private Integer interval;


}
