package com.dhcc.copywork.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dhcc.copywork.entity.GameKill;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 游戏杀率 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2020-08-26
 */
@Mapper
public interface GameKillMapper extends BaseMapper<GameKill> {
    @Delete("truncate game_kill")
    void truncateGameKill();
}
