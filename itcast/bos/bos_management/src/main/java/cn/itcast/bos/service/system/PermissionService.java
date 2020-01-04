package cn.itcast.bos.service.system;

import cn.itcast.bos.domain.system.Permission;

import java.util.List;

public interface PermissionService {
    //查询数据
    List<Permission> findAll();
    //添加权限
    void save(Permission model);
}
