package com.baomidou.entity;

    import com.baomidou.mybatisplus.annotation.IdType;
    import java.time.LocalDateTime;
    import com.baomidou.mybatisplus.annotation.TableId;
    import java.io.Serializable;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.experimental.Accessors;

/**
* <p>
    * 游戏杀率
    * </p>
*
* @author author
* @since 2020-08-26
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class GameKill implements Serializable {

    private static final long serialVersionUID = 1L;

            /**
            * 游戏ID
            */
            @TableId(value = "game_id", type = IdType.ID_WORKER)
    private Integer gameId;

            /**
            * 游戏编码
            */
    private String code;

            /**
            * 杀率次数
            */
    private Integer killCount;

            /**
            * 添加时间
            */
    private LocalDateTime addTime;

            /**
            * 修改时间
            */
    private LocalDateTime updateTime;

            /**
            * 开奖次数
            */
    private Integer openCount;

            /**
            * 杀率频率
            */
    private Integer rate;


}
