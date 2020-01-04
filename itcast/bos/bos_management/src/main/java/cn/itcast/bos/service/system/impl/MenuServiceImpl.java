package cn.itcast.bos.service.system.impl;

import cn.itcast.bos.dao.system.MenuRepository;
import cn.itcast.bos.dao.system.RoleRepository;
import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.MenuService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * User: 李锦卓
 * Time: 2018/9/9 21:46
 */
@Service
@Transactional
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private RoleRepository roleRepository;
    //查询菜单那
    @Override
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }
    //添加菜单
    @Override
    public void save(Menu model) {
        menuRepository.save(model);
    }
    //根据用户查询菜单
    @Override
    public List<Menu> findByUser(User user) {
        //针对admin用户显示所有菜单
        if (user.getUsername().equals("admin")) {
            return menuRepository.findAll();
        } else {
            Set<Role> roles = user.getRoles();
            List<Menu>list = new ArrayList<Menu>();
            for (Role role : roles) {
                Set<Menu> menus = role.getMenus();
                for (Menu menu : menus) {
                    list.add(menu);
                }
            }
            return list;
        }
    }
}