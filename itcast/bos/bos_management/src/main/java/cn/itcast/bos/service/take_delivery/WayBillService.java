package cn.itcast.bos.service.take_delivery;

import cn.itcast.bos.domain.take_delivery.WayBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WayBillService {
    //保存wayBill
    void save(WayBill model);
    //根据运单号查询运单
    WayBill findOne(String wayBillNum);
    //条件查询
    Page<WayBill> findPage(WayBill model, Pageable pageable);
    //根据order id 查找waybill
    WayBill findByOrderId(Integer id);
    //导出xls表格
    List<WayBill> findWayBills(WayBill model);
}
