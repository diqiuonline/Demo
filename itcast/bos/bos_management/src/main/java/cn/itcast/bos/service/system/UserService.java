package cn.itcast.bos.service.system;

import cn.itcast.bos.domain.system.User;

import java.util.List;

public interface UserService {
    //根据用户名查找用户
    User findByUsername(String username);
    //查询全部用户
    List<User> findAll();
    //添加用户
    void save(User model, String[] roleIds);
}
