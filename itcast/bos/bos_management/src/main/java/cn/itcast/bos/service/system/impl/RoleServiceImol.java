package cn.itcast.bos.service.system.impl;

import cn.itcast.bos.dao.system.MenuRepository;
import cn.itcast.bos.dao.system.PermissionRepository;
import cn.itcast.bos.dao.system.RoleRepository;
import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.service.system.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * User: 李锦卓
 * Time: 2018/9/10 19:26
 */
@Service
@Transactional
public class RoleServiceImol implements RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private MenuRepository menuRepository;
    //查询数据
    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }
    //保存角色
    @Override
    public void save(Role role, String[] permissionIds, String menuIds) {
        //保存角色
        roleRepository.save(role);
        //给角色关联权限
        if (permissionIds != null) {
            for (String permissionId : permissionIds) {
                role.getPermissions().add(permissionRepository.findOne(Integer.parseInt(permissionId)));
            }
        }
        //关联菜单
        if (menuIds != null) {
            String[] menuIdss = menuIds.split(",");
            for (String menuId : menuIdss) {
                role.getMenus().add(menuRepository.findOne(Integer.parseInt(menuId)));
            }
        }
    }
}