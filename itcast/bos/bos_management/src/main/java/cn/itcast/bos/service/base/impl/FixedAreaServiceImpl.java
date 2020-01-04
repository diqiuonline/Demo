package cn.itcast.bos.service.base.impl;

import cn.itcast.bos.dao.base.CourierRepository;
import cn.itcast.bos.dao.base.FixedAreaRepository;
import cn.itcast.bos.dao.base.TakeTimeRepositroy;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.service.base.FixedAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: 李锦卓
 * Time: 2018/8/23 0:33
 */
@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService {
    @Autowired
    private FixedAreaRepository fixedAreaRepository;
    @Override
    public void save(FixedArea model) {
        fixedAreaRepository.save(model);
    }
    //条件查询
    @Override
    @Cacheable(value = "standard" ,key = "#specification+'_'+#pageable")
    public Page<FixedArea> pageQuery(Specification specification, Pageable pageable) {
        return fixedAreaRepository.findAll(specification,  pageable);
    }
    //删除数据
    @Override
    public void del(String[] idArrer) {
        for (String id:idArrer){
            fixedAreaRepository.delete(id);
        }
    }
    @Autowired
    private TakeTimeRepositroy takeTimeRepositroy;
    @Autowired
    private CourierRepository courierRepository;


    //快递员排班动能
    @Override
    public void associationCourierToFixedArea(Integer courierId, Integer takeTimeId, FixedArea fixedArea) {
        //courierRepository
        //获取当前定区
        FixedArea one = fixedAreaRepository.findOne(fixedArea.getId());
        //定区关联快递员  多对多 外键由定区维护 定区关联快递员
        one.getCouriers().add(courierRepository.findOne(courierId));
        //快递员关联收派时间  多对一 外键由多方维护  快递员关联收派时间
        courierRepository.findOne(courierId).setTakeTime(takeTimeRepositroy.findOne(takeTimeId));
    }

}