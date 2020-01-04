package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.base.Area;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * User: 李锦卓
 * Time: 2018/8/22 21:14
 */
public interface AreaService {
    //批量保存
    public void saveBatch(List<Area> areas);
    //查询
    Page<Area> findPageData(Specification<Area> specification, Pageable pageable);
}