package com.dhcc.mp.simple.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dhcc.mp.simple.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper extends MyBaseMapper<User> {
    User findById(Long id);


}
