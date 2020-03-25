package com.dhcc.Context.dao;

import com.dhcc.Context.domain.Context;
import com.dhcc.Context.domain.ContextExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface ContextMapper {
    int countByExample(ContextExample example);

    int deleteByExample(ContextExample example);

    int deleteByPrimaryKey(String id);

    int insert(Context record);

    int insertSelective(Context record);

    List<Context> selectByExampleWithBLOBs(ContextExample example);

    List<Context> selectByExample(ContextExample example);

    Context selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Context record, @Param("example") ContextExample example);

    int updateByExampleWithBLOBs(@Param("record") Context record, @Param("example") ContextExample example);

    int updateByExample(@Param("record") Context record, @Param("example") ContextExample example);

    int updateByPrimaryKeySelective(Context record);

    int updateByPrimaryKeyWithBLOBs(Context record);

    int updateByPrimaryKey(Context record);
}