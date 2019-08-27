package com.itheima.store.dao;

import com.itheima.store.domain.User;

import java.sql.SQLException;

public interface UserDao {
    //注册添加数据
    void save(User user) throws SQLException;
    //异步校验用户名是否存在
    User findByUsername(String username)throws SQLException;
    //激活用户（查找用户）
    User findByCode(String code)throws SQLException;
    //修改用户（更改激活状态）
    void update(User user) throws SQLException;
    //用户登陆
    User login(User user)throws SQLException;
}
