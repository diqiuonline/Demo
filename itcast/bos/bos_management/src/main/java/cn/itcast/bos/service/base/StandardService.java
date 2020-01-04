package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.base.Standard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StandardService {
	//添加标准
	public void save(Standard standard);
	//分页查询
	Page<Standard> findPageDate(Pageable pageable);
	//标准查询
	List<Standard> findAll();
}
