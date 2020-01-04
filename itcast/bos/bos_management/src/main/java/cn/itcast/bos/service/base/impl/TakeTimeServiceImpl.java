package cn.itcast.bos.service.base.impl;

import cn.itcast.bos.dao.base.TakeTimeRepositroy;
import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.service.base.TakeTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User: 李锦卓
 * Time: 2018/8/26 19:16
 */
@Service
@Transactional
public class TakeTimeServiceImpl implements TakeTimeService {
    //属性注入
    @Autowired
    private TakeTimeRepositroy takeTimeRepositroy;
    //查询所有
    @Override
    public List<TakeTime> findAll() {
        return takeTimeRepositroy.findAll();
    }
}