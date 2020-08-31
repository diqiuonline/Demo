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
    public class PlayCopy1 implements Serializable {

    private static final long serialVersionUID = 1L;

            @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

            /**
            * 名称
            */
    private String name;

            /**
            * 别名
            */
    private String alias;

            /**
            * 值
            */
    private String value;

            /**
            * 游戏ID
            */
    private Integer gameId;

            /**
            * 默认赔率
            */
    private Double odds;

            /**
            * 最小赔率
            */
    private Double minOdds;

            /**
            * 最大赔率
            */
    private Double maxOdds;

            /**
            * 排序
            */
    private Integer sort;

            /**
            * 玩法名称
            */
    private String cateName;

            /**
            * 玩法编码
            */
    private String code;

            /**
            * 模式0官方1信用
            */
    private Integer model;

            /**
            * 公司抽水
            */
    private Double companyRebate;

            /**
            * 最大返水
            */
    private Double rebate;

            /**
            * 原始编码
            */
    private String codeBase;

            /**
            * 组
            */
    private String groupCode;


}
