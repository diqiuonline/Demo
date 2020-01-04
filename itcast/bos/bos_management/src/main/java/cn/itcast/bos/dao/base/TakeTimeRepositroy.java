package cn.itcast.bos.dao.base;

import cn.itcast.bos.domain.base.TakeTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TakeTimeRepositroy extends JpaRepository<TakeTime,Integer>,JpaSpecificationExecutor<TakeTime> {
}
