package cn.itcast.bos.service.system.impl;

import cn.itcast.bos.dao.system.PermissionRepository;
import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.service.system.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User: 李锦卓
 * Time: 2018/9/10 19:02
 */
@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;
    //查询权限
    @Override
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }
    //添加权限
    @Override
    public void save(Permission model) {
        permissionRepository.save(model);
    }
}