package com.dhcc.shop.dao;

import com.dhcc.shop.domain.TradeUser;
import com.dhcc.shop.domain.TradeUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TradeUserMapper {
    int countByExample(TradeUserExample example);

    int deleteByExample(TradeUserExample example);

    int deleteByPrimaryKey(Long userId);

    int insert(TradeUser record);

    int insertSelective(TradeUser record);

    List<TradeUser> selectByExample(TradeUserExample example);

    TradeUser selectByPrimaryKey(Long userId);

    int updateByExampleSelective(@Param("record") TradeUser record, @Param("example") TradeUserExample example);

    int updateByExample(@Param("record") TradeUser record, @Param("example") TradeUserExample example);

    int updateByPrimaryKeySelective(TradeUser record);

    int updateByPrimaryKey(TradeUser record);
}