package com.dhcc.copywork.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dhcc.copywork.entity.Game;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author author
 * @since 2020-07-22
 */
@Mapper
public interface GameMapper extends BaseMapper<Game> {
    //@Delete("truncate game;truncate play;truncate play_cate;truncate game_time")
    @Delete("drop table  game;" +
            "drop table  play;" +
            "drop table  play_cate;"+
            "drop table  game_time;"+
            "CREATE TABLE game      LIKE game_template;"+
            "CREATE TABLE play      LIKE play_template;"+
            "CREATE TABLE play_cate LIKE play_cate_template;"+
            "CREATE TABLE game_time LIKE game_time_template"
    )
    void truncateGameAndPlayAndPlayCateAndGameTime();
    @Select("INSERT INTO game      SELECT * FROM game_template        where id = #{gameId};" +
            "INSERT INTO play      SELECT * FROM play_template        where game_id = #{gameId};"+
            "INSERT INTO play_cate SELECT * FROM play_cate_template   where game_id = #{gameId};"+
            "INSERT INTO game_time SELECT * FROM game_time_template   where game_id = #{gameId}"
    )
    void copyGameAndPlayAndPlayCateAndGameTime(int gameId);
}
