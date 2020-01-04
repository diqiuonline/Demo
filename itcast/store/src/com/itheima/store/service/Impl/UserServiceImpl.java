package com.itheima.store.service.Impl;

import com.itheima.store.dao.Impl.UserDaoImpl;
import com.itheima.store.dao.UserDao;
import com.itheima.store.domain.User;
import com.itheima.store.service.UserService;
import com.itheima.store.utils.BeanFactory;
import com.itheima.store.utils.MailUtils;
import com.itheima.store.utils.UUIDUtils;

import java.sql.SQLException;

public class UserServiceImpl implements UserService {
    //注册添加数据
    @Override
    public void save(User user) throws SQLException {
        UserDao userDao = (UserDao) BeanFactory.getBean("userDao");
        user.setUid(UUIDUtils.getUUID());
        user.setState(1);
        user.setCode(UUIDUtils.getUUID()+UUIDUtils.getUUID());
        userDao.save(user);

        //发送邮件
        MailUtils.sendMail(user.getEmail(),user.getCode());
    }
    //异步校验用户名是否存在
    @Override
    public User findByUsername(String username) throws SQLException {
        //调用数据库查询
        UserDao userDao = (UserDao) BeanFactory.getBean("userDao");
        User user = userDao.findByUsername(username);

        return user;
    }
    //激活用户（查找用户）
    @Override
    public User findVBycode(String code) throws SQLException {
        UserDao userDao = (UserDao) BeanFactory.getBean("userDao");
        User user = userDao.findByCode(code);
        return user;
    }
    //修改用户（更改激活状态）
    @Override
    public void update(User user) throws SQLException {
        UserDao userDao = (UserDao) BeanFactory.getBean("userDao");
        userDao.update(user);
    }
    //用户登陆
    @Override
    public User login(User user) throws SQLException {
        UserDao userDao = (UserDao) BeanFactory.getBean("userDao");
        User existUser = userDao.login(user);
        return existUser;
    }
    //

    //
}
