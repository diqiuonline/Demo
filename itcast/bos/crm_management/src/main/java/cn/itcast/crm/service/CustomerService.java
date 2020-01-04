package cn.itcast.crm.service;

import cn.itcast.crm.domain.Customer;

import javax.ws.rs.*;
import java.util.List;

public interface CustomerService {
    //查询所有未关联客户列表
    @Path("/noassociationcustomers")
    @GET
    @Produces({"application/xml","application/json"})
    public List<Customer> findNoAssociationCustomers();

    //已经关联到指定定区的客户列表
    @Path("/associationfixedareacustomers/{fixedareaid}")
    @GET
    @Produces({"application/xml", "application/json"})
    public List<Customer> findHasAssociationFixedAreaCustomers(@PathParam("fixedareaid") String fixedAreaId);

    //将客户关联到定区上 将所有客户id 拼成字符串 1，2，3
    @Path("/associationcustomerstofixedarea")
    public void associationCustomersToFixedArea(@QueryParam("customerIdStr") String customerIdStr,
                                                @QueryParam("fixedAreaId") String fixedAreaId);
    //保存客户
    @Path("/customer")
    @POST
    @Consumes({"application/xml","application/json"})
    void regist(Customer customer);

    //查询激活码客户
    @Path("/customer/telephone/{telephone}")
    @GET
    @Consumes({"application/xml","application/json"})
    Customer findByTelephone(@PathParam("telephone") String telephone);
    //修改状态
    @Path("/customer/updatetype/{telephone}")
    @GET
    void updateType(@PathParam("telephone") String telephone);
    //客户登陆
    @Path("/customer/login")
    @GET
    @Consumes({"application/xml","application/json"})
    Customer login(@QueryParam("telephone")String telephone,@QueryParam("password")String password);
    //根据地址查询定区编码
    @Path("/customer/findFixedAreaIdByAddress")
    @GET
    @Consumes({"application/xml", "application/json"})
    public String findFixedAreaIdByAddress(@QueryParam("address") String address);
}
