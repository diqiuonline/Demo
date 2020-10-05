package com.dhcc.copywork.mapper;

import com.dhcc.copywork.entity.UserBet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 今天投注 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2020-08-24
 */
public interface UserBetMapper extends BaseMapper<UserBet> {

    //INSERT INTO game      SELECT * FROM game_template        where id = #{gameId};" +
    @Update("CREATE TABLE ${userBetName}  LIKE user_bet;"+
            "CREATE TABLE ${userBillName}  LIKE user_bill;"
    )
    void addUserBetZeroToEnd(@Param("userBetName") String userBetName,@Param("userBillName") String userBillName);
}
