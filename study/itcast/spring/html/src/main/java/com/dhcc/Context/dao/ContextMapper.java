package com.dhcc.Context.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dhcc.Context.domain.Context;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface ContextMapper extends BaseMapper<Context>{

}