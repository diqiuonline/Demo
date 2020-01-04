package cn.itcast.bos.service.system.impl;

import cn.itcast.bos.dao.system.RoleRepository;
import cn.itcast.bos.dao.system.UserRepository;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User: 李锦卓
 * Time: 2018/9/9 19:22
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    //根据用户名查找用户
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    //查询全部用户
    @Override
    @RequiresPermissions("region:list")
    public List<User> findAll() {
        return userRepository.findAll();
    }
    //添加用户
    @Override
    public void save(User user, String[] roleIds) {
        userRepository.save(user);
        //添加角色
        for (String roleId : roleIds) {
            user.getRoles().add(roleRepository.findOne(Integer.parseInt(roleId)));
        }
    }
}