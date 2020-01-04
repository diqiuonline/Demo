package cn.itcast.bos.service.transit.Impl;

import cn.itcast.bos.dao.transit.SignInfoRepository;
import cn.itcast.bos.dao.transit.TransitInfoRepository;
import cn.itcast.bos.domain.transit.SignInfo;
import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.index.WayBillIndexRepository;
import cn.itcast.bos.service.transit.SignInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: 李锦卓
 * Time: 2018/9/18 18:58
 */
@Service
@Transactional
public class SignInfoServiceImpl implements SignInfoService {
    @Autowired
    private SignInfoRepository signInfoRepository;
    @Autowired
    private TransitInfoRepository transitInfoRepository;
    @Autowired
    private WayBillIndexRepository wayBillIndexRepository;
    //保存派件信息
    @Override
    public void save(String transitInfoId, SignInfo model) {
        signInfoRepository.save(model);
        //关联运输流程
        TransitInfo transitInfo = transitInfoRepository.findOne(Integer.parseInt(transitInfoId));
        transitInfo.setSignInfo(model);
        //更改状态
        if (model.getSignType().equals("正常")) {
            //正常签收
            transitInfo.setStatus("正常签收");
            //更改运单状态
            transitInfo.getWayBill().setSignStatus(3);
            //更改索引库
            wayBillIndexRepository.save(transitInfo.getWayBill());
        } else {
            transitInfo.setStatus("异常");
            transitInfo.getWayBill().setSignStatus(4);
            wayBillIndexRepository.save(transitInfo.getWayBill());
        }
    }
}