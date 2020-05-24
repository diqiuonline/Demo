package com.dhcc.mp.simple.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dhcc.mp.simple.pojo.User;

import java.util.List;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/5/24 20:17
 */
public interface MyBaseMapper<T> extends BaseMapper<T> {
    List<T> findAll();
    //扩展其他的方法
}
