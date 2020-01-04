package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.base.FixedArea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public interface FixedAreaService {
    //保存定区
    void save(FixedArea model);
    //条件查询
    Page<FixedArea> pageQuery(Specification specification, Pageable pageable);
    //删除数据
    void del(String[] idArrer);
    //快递员排班功能
    void associationCourierToFixedArea(Integer courierId, Integer takeTimeId, FixedArea model);
}
