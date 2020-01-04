package cn.itcast.bos.dao.base;

import cn.itcast.bos.dao.take_delivery.WayBillRepository;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.index.WayBillIndexRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: 李锦卓
 * Time: 2018/9/12 22:12
 */

public class test {
    @Autowired
    private WayBillRepository wayBillRepository;
    @Autowired
    private WayBillIndexRepository wayBillIndexRepository;
    @Test
    public void demo() {
        WayBill one = wayBillRepository.findOne(139);
        wayBillIndexRepository.save(one);
    }
}