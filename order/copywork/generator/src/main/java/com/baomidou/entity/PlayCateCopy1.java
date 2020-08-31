package com.baomidou.entity;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import java.io.Serializable;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.experimental.Accessors;

/**
* <p>
    * 
    * </p>
*
* @author author
* @since 2020-07-23
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class PlayCateCopy1 implements Serializable {

    private static final long serialVersionUID = 1L;

            /**
            * 类别编码
            */
            @TableId(value = "code", type = IdType.ID_WORKER)
    private String code;

            /**
            * 玩法类别名称
            */
    private String name;

    private Integer gameId;

            /**
            * 是否显示名称0：显示、1不显示
            */
    private Integer isShow;

            /**
            * 排序
            */
    private Integer sort;

            /**
            * 层级(最好三级内)
            */
    private Integer level;

            /**
            * 是否封盘: 0-正常、1-封盘(六合彩需要根据分类来做封盘控制)
            */
    private Integer isBan;

            /**
            * 父ID
            */
    private Long superCode;

            /**
            * 父名称
            */
    private String superName;

            /**
            * 最后层级0否,1是
            */
    private Integer lastLayer;

            /**
            * 子级数量
            */
    private Integer lowerNum;

            /**
            * 是否为玩法0表示否,1表示是
            */
    private Integer isPlay;

            /**
            * 路径
            */
    private String paths;

            /**
            * 下注内容规则json
            */
    private String rules;

            /**
            * 返水
            */
    private Double rebate;

            /**
            * 单注下注最低金额
            */
    private Double minMoney;

            /**
            * 单注下注最高金额
            */
    private Double maxMoney;

            /**
            * 当期下注金额最大金额
            */
    private Double maxTurnMoney;

            /**
            * 开奖处理类型1用玩法批量处理开奖,2单注处理
            */
    private Integer handleType;

            /**
            * 是否将该玩法记录在开奖结果中：0不统计，1统计
            */
    private Integer isRecordInLottery;

            /**
            * 是否统计长龙：0不统计，1统计
            */
    private Integer isCountCl;

            /**
            * 是否统计两面长龙数据：0不统计，1统计
            */
    private Integer isCountLmcl;

            /**
            * 赔率设置类型:0表示无,1表示跟据code,2表示跟据父ID设置
            */
    private Integer rateType;

            /**
            * 模式：0官方玩法,1信用玩法
            */
    private Integer model;

            /**
            * 基础编码
            */
    private String codeBase;

            /**
            * 是关闭玩法0正常1关闭
            */
    private Integer close;


}
