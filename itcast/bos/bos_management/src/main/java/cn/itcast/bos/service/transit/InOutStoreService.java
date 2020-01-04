package cn.itcast.bos.service.transit;

import cn.itcast.bos.domain.transit.InOutStorageInfo;

public interface InOutStoreService {
    void save(String transitInfoId, InOutStorageInfo model);
}
