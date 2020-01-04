package cn.itcast.bos.service.transit.Impl;

import cn.itcast.bos.dao.transit.DeliveryInfoRepository;
import cn.itcast.bos.dao.transit.TransitInfoRepository;
import cn.itcast.bos.domain.transit.DeliveryInfo;
import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.service.transit.DeliveryInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: 李锦卓
 * Time: 2018/9/18 18:20
 */
@Service
@Transactional
public class DeliveryInfoServiceImpl implements DeliveryInfoService {
    @Autowired
    private DeliveryInfoRepository deliveryInfoRepository;
    @Autowired
    private TransitInfoRepository transitInfoRepository;
    //保存配送信息
    @Override
    public void save(String transitInfoId, DeliveryInfo model) {
        deliveryInfoRepository.save(model);
        //查询运输配送搞对象
        TransitInfo transitInfo = transitInfoRepository.findOne(Integer.parseInt(transitInfoId));
        transitInfo.setDeliveryInfo(model);
        transitInfo.setStatus("开始配送");
    }
}