package com.itheima.autologin.dao;


import com.itheima.autologin.domin.UserPojo;
import com.utils.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.SQLException;


public class UserDao {

	public void register(UserPojo user) throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner queryrunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "insert into user(username,password) values(?,?)";
		//UserPojo query = queryrunner.query(sql, new BeanHandler<UserPojo>(UserPojo.class), user.getUsername(),user.getPassword());
		queryrunner.update(sql, user.getUsername(),user.getPassword());
		
	}

	public UserPojo login(UserPojo user) throws SQLException {
		QueryRunner queryrunner = new QueryRunner(JDBCUtils.getDataSource());
		String sql = "select * from user where username = ? and password = ?";
		UserPojo query = queryrunner.query(sql, new BeanHandler<UserPojo>(UserPojo.class),user.getUsername(),user.getPassword());
		//UserPojo query = queryrunner.query(sql, new BeanHandler<UserPojo>(UserPojo.class), user.getUsername(),user.getPassword());
		return query;
	}

}
