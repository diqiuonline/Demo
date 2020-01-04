package cn.itcast.crm.service.impl;

import cn.itcast.crm.dao.CustomerRepository;
import cn.itcast.crm.domain.Customer;
import cn.itcast.crm.service.CustomerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User: 李锦卓
 * Time: 2018/8/24 15:06
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    //注入dao
    @Autowired
    private CustomerRepository customerRepository;
    //查询所有未关联客户列表
    @Override
    public List<Customer> findNoAssociationCustomers() {
        return customerRepository.findByFixedAreaIdIsNull();
    }
    //查询所有已经关联到指定定区的客户列表
    @Override
    public List<Customer> findHasAssociationFixedAreaCustomers(String fixedAreaId) {
        return customerRepository.findByFixedAreaId(fixedAreaId);
    }
    //将客户关联到定区上
    @Override
    public void associationCustomersToFixedArea(String customerIdStr, String fixedAreaId) {
        //解除关联动作
        customerRepository.clearFixedAreaId(fixedAreaId);
        //切割字符串
        if (customerIdStr.equals("null")){
            System.out.println("123");
            return;
        }
        String[] customerIdArray = customerIdStr.split(",");
        for (String customerId:customerIdArray){
            Integer id = Integer.parseInt(customerId);
            customerRepository.updateFixedAreaId(fixedAreaId,id);
        }
    }
    //保存客户
    @Override
    public void regist(Customer customer) {
        customerRepository.save(customer);
    }
    //查询客户
    @Override
    public Customer findByTelephone(String telephone) {
        System.out.println("21432");
        return customerRepository.findByTelephone(telephone);
    }
    //绑定邮箱
    @Override
    public void updateType(String telephone) {
        System.out.println("23445");
        customerRepository.updateType(telephone);
    }
    //登陆客户
    @Override
    public Customer login(String telephone, String password) {
        //customerRepository.login()
        return customerRepository.findByTelephoneAndPassword(telephone,password);
    }

    //根据地址查询定区编码
    @Override
    public String findFixedAreaIdByAddress(String address) {
        System.out.println(address);
        return customerRepository.findFixedAreaIdByAddress(address);
    }


}