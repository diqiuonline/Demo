package cn.itcast.bos.service.system;

import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.User;

import java.util.List;

public interface MenuService {
    //查询所有菜单
    List<Menu> findAll();
    //添加菜单
    void save(Menu model);
    //根据用户查询菜单
    List<Menu> findByUser(User user);
}
