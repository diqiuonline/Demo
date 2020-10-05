package com.baomidou.entity;

    import com.baomidou.mybatisplus.annotation.IdType;
    import java.time.LocalDate;
    import com.baomidou.mybatisplus.annotation.TableId;
    import java.time.LocalDateTime;
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
* @since 2020-08-28
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class UserBetCountUodate implements Serializable {

    private static final long serialVersionUID = 1L;

            @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private LocalDateTime created;

    private LocalDateTime updated;

            /**
            * 投注用户ID
            */
    private Integer userId;

            /**
            * 投注日期
            */
    private LocalDate byDate;

            /**
            * 投注次数
            */
    private Integer betCount;

            /**
            * 日投注金额
            */
    private Double betAmount;

            /**
            * 日中奖次数
            */
    private Integer winCount;

            /**
            * 日中奖金额
            */
    private Double winAmount;

            /**
            * 当日胜率
            */
    private Float winRate;


}
