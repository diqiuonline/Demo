package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.base.TakeTime;

import java.util.List;

public interface TakeTimeService {
    //查询所有
    List<TakeTime> findAll();
}
