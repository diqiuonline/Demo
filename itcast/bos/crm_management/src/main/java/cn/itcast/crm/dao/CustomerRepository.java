package cn.itcast.crm.dao;

import cn.itcast.crm.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * User: 李锦卓
 * Time: 2018/8/24 15:10
 */
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    //查询所有未关联客户列表
    List<Customer> findByFixedAreaIdIsNull();
    //查询所有已经关联到指定定区的客户列表
    List<Customer> findByFixedAreaId(String fixedAreaId);
    //清除关联动作
    @Query("update Customer set fixedAreaId = null where fixedAreaId = ?")
    @Modifying
    void clearFixedAreaId(String fixedAreaId);
    //添加关联动作
    @Query("update Customer set fixedAreaId = ? where id = ?")
    @Modifying
    void updateFixedAreaId(String fixedAreaId, Integer id);
    //查询客户
    Customer findByTelephone(String telephone);
    //绑定邮箱
    @Query("update Customer set type = 1 where telephone = ?")
    @Modifying
    void updateType(String telephone);
    //账户登陆
    Customer findByTelephoneAndPassword(String telephone, String password);
    //根据地址查询定区编码
    @Query("select fixedAreaId from Customer where address = ?")
    String findFixedAreaIdByAddress(String address);
}