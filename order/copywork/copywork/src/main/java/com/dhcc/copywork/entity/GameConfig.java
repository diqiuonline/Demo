package com.dhcc.copywork.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
* <p>
    * 
    * </p>
*
* @author author
* @since 2020-07-28
*/
    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class GameConfig implements Serializable {

    private static final long serialVersionUID = 1L;

            /**
            * 游戏编号
            */
            @TableId(value = "id", type = IdType.ID_WORKER)
    private Integer id;

            /**
            * 游戏名称
            */
    private String name;

            /**
            * 游戏代码
            */
    private String code;

            /**
            * 对应的模板
            */
    private Integer templateGameId;

            /**
            * 极速间隔(秒)
            */
    private Integer jsIntervalSeconds;

            /**
            * 开售延迟秒数
            */
    private Integer timeStartDelay;

            /**
            * 提前截止秒数
            */
    private Integer timeEndEarly;

            /**
            * 在直播间显示
            */
    private Integer isZbj;

            /**
            * 在游戏中心显示
            */
    private Integer isYxzx;


}
