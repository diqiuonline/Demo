package cn.itcast.bos.service.base.impl;

import cn.itcast.bos.dao.base.AreaRepository;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User: 李锦卓
 * Time: 2018/8/22 21:33
 */
@Service
@Transactional
public class AreaServiceImpl implements AreaService {
    //注入dao
    @Autowired
    private AreaRepository areaRepository;
    //批量保存
    @Override
    public void saveBatch(List<Area> areas) {
        areaRepository.save(areas);
    }
    //查询
    @Override
    public Page<Area> findPageData(Specification<Area> specification, Pageable pageable) {
        return areaRepository.findAll(specification,pageable);
    }
}