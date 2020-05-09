package com.dhcc.mp.simple.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dhcc.mp.simple.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    User findById(Long id);
}
