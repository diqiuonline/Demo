package com.dhcc.mp.simple.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dhcc.mp.simple.pojo.User;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    List<User> findAll();
}
