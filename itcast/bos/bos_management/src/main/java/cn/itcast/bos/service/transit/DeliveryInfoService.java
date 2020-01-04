package cn.itcast.bos.service.transit;

import cn.itcast.bos.domain.transit.DeliveryInfo;

public interface DeliveryInfoService {
    //保存配送信息
    void save(String transitInfoId, DeliveryInfo model);
}
