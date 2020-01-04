package cn.itcast.bos.service.system;

import cn.itcast.bos.domain.system.Role;

import java.util.List;

public interface RoleService {
    //查询数据
    List<cn.itcast.bos.domain.system.Role> findAll();
    //保存角色
    void save(Role role, String[] permissionIds, String menuIds);
}
