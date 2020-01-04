package com.itheima.store.dao.Impl;

import com.itheima.store.dao.UserDao;
import com.itheima.store.domain.User;
import com.itheima.store.utils.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.SQLException;

public class UserDaoImpl implements UserDao {
    //注册添加数据
    @Override
    public void save(User user) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "insert into user values(?,?,?,?,?,?,?,?,?,?)";
        Object[] obj = {user.getUid(),user.getUsername(),user.getPassword(),user.getName(),
                user.getEmail(),user.getTelephone(),user.getBirthday(),user.getSex(),user.getState(),user.getCode()};
        queryRunner.update(sql,obj);
    }
    //异步校验用户名是否存在
    @Override
    public User findByUsername(String username) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "select * from user where username = ?";
        User user = queryRunner.query(sql, new BeanHandler<User>(User.class), username);
        return user;
    }
    //激活用户(查找用户)
    @Override
    public User findByCode(String code) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "select * from user where code = ?";
        User user = queryRunner.query(sql, new BeanHandler<User>(User.class), code);
        return user;
    }
    //修改用户（更改激活状态）
    @Override
    public void update(User user) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "update user set username = ?,password = ?,name = ?,email = ?,telephone = ?,birthday = ?,sex = ?," +
                "state =?,code = ? where uid = ?";
        Object[] obj = {user.getUsername(),user.getPassword(),user.getName(),user.getEmail(),user.getTelephone(),
                user.getBirthday(),user.getSex(),user.getState(),user.getCode(),user.getUid()};
        queryRunner.update(sql,obj);
    }
    //用户登陆
    @Override
    public User login(User user) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "select * from user where username = ? and password = ? and state = ?";
        User query = queryRunner.query(sql, new BeanHandler<User>(User.class), user.getUsername(), user.getPassword(),0);
        return query;
    }

}
