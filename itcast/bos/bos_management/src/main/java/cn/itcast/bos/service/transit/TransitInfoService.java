package cn.itcast.bos.service.transit;

import cn.itcast.bos.domain.transit.TransitInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransitInfoService {
    //保存Transit
    void createTransit(String wayBillIds);
    //定时更行数据库
    void synxIndex();
    //查询
    Page<TransitInfo> findPage(Pageable pageable);
}
