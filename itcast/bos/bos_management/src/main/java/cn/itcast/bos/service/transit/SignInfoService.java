package cn.itcast.bos.service.transit;

import cn.itcast.bos.domain.transit.SignInfo;

public interface SignInfoService {
    //保存派件信息
    void save(String transitInfoId, SignInfo model);
}
