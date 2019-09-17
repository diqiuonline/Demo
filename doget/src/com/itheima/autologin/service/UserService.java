package com.itheima.autologin.service;


import com.itheima.autologin.dao.UserDao;
import com.itheima.autologin.domin.UserPojo;

import java.sql.SQLException;

public class UserService {

	public void register(UserPojo user) throws SQLException {
		UserDao usersql = new UserDao();
		usersql.register(user);
	}

	public UserPojo login(UserPojo user) throws SQLException {
		UserDao ud = new UserDao();
		UserPojo up = ud.login(user);
		// TODO Auto-generated method stub
		return up;
	}

	

}
