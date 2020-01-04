package cn.itcast.bos.service.base.impl;

import cn.itcast.bos.dao.base.StandardRepository;
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User: 李锦卓
 * Time: 2018/8/18 21:08
 */
@Service
@Transactional
public class StandardServiceImpl implements StandardService {
	//注入dao
	@Autowired
	private StandardRepository standardRepository;
	//添加标准
	@Override
    @CacheEvict(value = "standard",allEntries = true )
	public void save(Standard standard) {
		standardRepository.save(standard);
	}
	//分页查询
	@Override
    @Cacheable(value = "standard" ,key = "#pageable.pageNumber+'_'+#pageable.pageSize")
	public Page<Standard> findPageDate(Pageable pageable) {
		return standardRepository.findAll(pageable);
	}
	//标准查询
	@Override
    @Cacheable("standard")
	public List<Standard> findAll() {
		return standardRepository.findAll();
	}
}