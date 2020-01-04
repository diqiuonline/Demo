package cn.itcast.bos.dao.base;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.crm.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourierRepository extends JpaRepository<Courier,Integer>,JpaSpecificationExecutor<Courier> {
    //作废快递员
    @Query(value = "update Courier set deltag='1' where id = ?")
    @Modifying
    public void delBeath(Integer id);
    //恢复快递员
    @Query(value = "update Courier set deltag=null where id = ?")
    @Modifying
    public void redBeath(Integer id);

}
