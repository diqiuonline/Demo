package cn.itcast.bos.service.transit.Impl;

import cn.itcast.bos.dao.take_delivery.WayBillRepository;
import cn.itcast.bos.dao.transit.TransitInfoRepository;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.index.WayBillIndexRepository;
import cn.itcast.bos.service.transit.TransitInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User: 李锦卓
 * Time: 2018/9/12 20:21
 */
@Service
@Transactional
public class TransitInfoServiceImpl implements TransitInfoService {
    @Autowired
    private WayBillRepository wayBillRepository;
    @Autowired
    private WayBillIndexRepository wayBillIndexRepository;
    @Autowired
    private TransitInfoRepository transitInfoRepository;
    //保存transit
    @Override
    public void createTransit(String wayBillIds) {
        if (StringUtils.isNoneBlank(wayBillIds)) {
            for (String wayBillId : wayBillIds.split(",")) {
                WayBill wayBill = wayBillRepository.findOne(Integer.parseInt(wayBillId));
                //判断运单状态是否时待发货
                if (wayBill.getSignStatus() == 1) {
                    //代发货
                    TransitInfo transitInfo = new TransitInfo();
                    transitInfo.setWayBill(wayBill);
                    transitInfo.setStatus("出库中转");
                    transitInfoRepository.save(transitInfo);
                    //更改运单状态
                    wayBill.setSignStatus(2); //配送中
                    //同步索引库
                    wayBillIndexRepository.save(wayBill);
                } else {
                    throw new RuntimeException("运单已经出发");
                }
            }
        }
    }
    //定时跟新数据库
    @Override
    public void synxIndex() {
        //查询数据库
        List<WayBill> list = wayBillRepository.findAll();
        wayBillIndexRepository.save(list);
    }
    //查询数据库列表
    @Override
    public Page<TransitInfo> findPage(Pageable pageable) {
        return transitInfoRepository.findAll(pageable);
    }
}