package com.dhcc.copywork.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
* <p>
    * 今天投注
    * </p>
*
* @author author
* @since 2020-08-24
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class UserBet implements Serializable {

    private static final long serialVersionUID = 1L;

            @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Integer doState;

    private Integer doSeq;

            /**
            * 投注订单号(主键)
            */
    private String orderNo;

    private Integer packId;

    private String packNo;

            /**
            * 账号编号
            */
    private Long userId;

            /**
            * 账号
            */
    private String account;

    private String userName;

            /**
            * 用户真实姓名
            */
    private String fullName;

            /**
            * 1级代理
            */
    private Long dlId;

            /**
            * 1级代理账号
            */
    private String dlName;

            /**
            * 代理路径
            */
    private String userPaths;

            /**
            * 玩法分类code
            */
    private String cateCode;

            /**
            * 玩法名称
            */
    private String cateName;

            /**
            * 玩法路径
            */
    private String catePaths;

            /**
            * 投注内容
            */
    private String betInfo;

            /**
            * 每注金额
            */
    private Double money;

            /**
            * 下注总数(组选有多组)
            */
    private Integer totalNums;

            /**
            * 投注总金额
            */
    private Double totalMoney;

            /**
            * 赔率(多个赔率用/分隔)
            */
    private String odds;

            /**
            * 中奖几注数
            */
    private Integer winCount;

            /**
            * 返水
            */
    private Double rebate;

            /**
            * 投注时间
            */
    private LocalDateTime addTime;

            /**
            * 期号
            */
    private String turnNum;

            /**
            * 游戏类型
            */
    private Integer gameId;

            /**
            * 投注状态:0-未开奖;1-已开奖;2-撤销；3-删除
            */
    private Integer status;

            /**
            * 中奖金额（不包括返点、包括本金）
            */
    private Double reward;

            /**
            * 合局金额
            */
    private Double drawMoney;

            /**
            * 返水金额
            */
    private Double rebateMoney;

            /**
            * 投注来源:0-网页;1-手机浏览器;2-android;3-ios;4-客户端
            */
    private Integer betSrc;

            /**
            * 开奖号码
            */
    private String openNum;

            /**
            * 中奖结果:0-没中;1-中奖;2-和局
            */
    private Integer result;

            /**
            * 校验字段
            */
    private String fk;

            /**
            * 开奖时间
            */
    private LocalDateTime openTime;

    private LocalDate statTime;

            /**
            * 会员类型HY会员TEST试玩会员
            */
    private String userType;

            /**
            * 账变订单号
            */
    private String billOrderNo;

            /**
            * 是否被后台修改
            */
    private Boolean modified;

            /**
            * 开奖计算公式
            */
    private String lotteryFormula;

            /**
            * 总代理账号
            */
    private String zdlName;

            /**
            * 股东账号
            */
    private String gdName;

            /**
            * 大股东账号
            */
    private String dgdName;

            /**
            * 分表ID
            */
    private Integer tableIndex;

            /**
            * 下注开始时间
            */
    private LocalDateTime betStartTime;

            /**
            * 下注结束时间
            */
    private LocalDateTime betEndTime;

            /**
            * 倍数
            */
    private Integer multiple;

            /**
            * 模式:0元1角2分
            */
    private Integer betModel;

            /**
            * 追号订单号
            */
    private String trackOrderNo;

            /**
            * 模式：0官方玩法,1信用玩法
            */
    private Integer model;

            /**
            * 投注位数如时时彩(万位、千位、百位、十位、个位)值为(1,2,3,4,5)
            */
    private String poschoose;

            /**
            * 玩法赔率说明多个值用逗号分隔开
            */
    private String oddsName;

            /**
            * 游戏(别名)ID：游戏ID|模式
            */
    private Integer aliasId;

            /**
            * 投注位数名称
            */
    private String poschooseName;

            /**
            * 所属上级账号
            */
    private String superName;

            /**
            * 是否黑名单用户0否,1是
            */
    private Integer black;


}
