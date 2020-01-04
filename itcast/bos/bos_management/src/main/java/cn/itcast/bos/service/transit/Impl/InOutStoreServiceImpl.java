package cn.itcast.bos.service.transit.Impl;

import cn.itcast.bos.dao.transit.InOutStoreRepository;
import cn.itcast.bos.dao.transit.TransitInfoRepository;
import cn.itcast.bos.domain.transit.InOutStorageInfo;
import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.service.transit.InOutStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: 李锦卓
 * Time: 2018/9/18 15:46
 */
@Service
@Transactional
public class InOutStoreServiceImpl implements InOutStoreService {
    @Autowired
    private InOutStoreRepository inOutStoreRepository;
    @Autowired
    private TransitInfoRepository transitInfoRepository;

    //保存出入库信息
    @Override
    public void save(String transitInfoId, InOutStorageInfo model) {
        //保存出入库信息
        inOutStoreRepository.save(model);
        //查询TransitInfo
        TransitInfo transitInfo = transitInfoRepository.findOne(Integer.parseInt(transitInfoId));
        //关联出入库信息 到运输配送对象
        transitInfo.getInOutStorageInfos().add(model);
        //修改状态
        if (model.getOperation().equals("到达网点")) {
            transitInfo.setStatus("到达网点");
            //更新网点地址 显示配送路径
            transitInfo.setOutletAddress(model.getAddress());
        }
    }
}