package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.service.base.FixedAreaService;
import cn.itcast.bos.web.action.common.BaseAction;
import cn.itcast.crm.domain.Customer;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: 李锦卓
 * Time: 2018/8/23 0:25
 */
@ParentPackage("struts-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class FixedAreaAction extends BaseAction<FixedArea> {
    //注入service
    @Autowired
    private FixedAreaService fixedAreaService;

    //条件定区
    @Action(value = "fixedArea_save", results = {@Result(name = "success", location = "/pages/base/fixed_area.html")})
    public String save() {
        fixedAreaService.save(model);
        return SUCCESS;
    }
    //条件查询
    @Action(value = "fixedArea_pageQuery",results = {@Result(name = "success",location = "/pages/base/fixed_area.html")})
    public void pageQuery() throws IOException {
        ServletActionContext.getResponse().setContentType("html/text;charset=utf-8");
        Pageable pageable = new PageRequest(page-1,rows);
        //构造条件查询对象
        Specification specification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                //创建保存条件的集合
                List<Predicate>list = new ArrayList<Predicate>();
                //添加条件
                if (StringUtils.isNoneBlank(model.getId())) {
                    //根据定区编码查询
                    Predicate p1 = cb.equal(root.get("id").as(String.class), model.getId());
                    list.add(p1);
                }
                if (StringUtils.isNoneBlank(model.getCompany())){
                    //根据所属单位查询
                    Predicate p2 = cb.like(root.get("company").as(String.class), model.getCompany());
                    list.add(p2);
                }
                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        //调用业务层
        Page<FixedArea> result = fixedAreaService.pageQuery(specification,pageable);
        //转json
        PagetoMapToJson(result);
    }
    //属性驱动
    private String ids;

    public void setIds(String ids) {
        this.ids = ids;
    }
    //删除数据
    @Action(value = "fixedArea_del", results = {@Result(name = "success", location = "/pages/base/fixed_area.html")})
    public String del() {
        String[] idArrer = ids.split(",");
        fixedAreaService.del(idArrer);
        return SUCCESS;
    }
    //查询未关联定区列表
    @Action(value = "fixedArea_findNoAssociationCustomers")
    public void findNoAssociationCustomers() throws IOException {
        ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
        Collection<? extends Customer> collection =
                WebClient.create("http://localhost:9002/crm_management/services/customerService/noassociationcustomers")
                        .accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);
        String string = JSONObject.toJSONString(collection);
        ServletActionContext.getResponse().getWriter().println(string);
    }
    //查询已关联定区列表
    @Action(value = "fixedArea_findHasAssociationFixedAreaCustomers")
    public void fixedArea_findHasAssociationFixedAreaCustomers() throws IOException {
        ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
        Collection<? extends Customer> collection =
                WebClient.create("http://localhost:9002/crm_management/services/customerService/associationfixedareacustomers/"+model.getId())
                        .accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).getCollection(Customer.class);
        String string = JSONObject.toJSONString(collection);
        ServletActionContext.getResponse().getWriter().println(string);
    }
    //属性驱动
    private String[] customerIds;

    public void setCustomerIds(String[] customerIds) {
        this.customerIds = customerIds;
    }

    //关联客户到定区
    @Action(value = "fixedArea_associationCustomersToFixedArea",
            results = {@Result(name = "success",type = "redirect",location = "/pages/base/fixed_area.html")})
    public String fixedArea_associationCustomersToFixedArea() {
        String customerIdStr = StringUtils.join(customerIds, ",");
        WebClient.create("http://localhost:9002/crm_management/services/customerService/" +
                "associationcustomerstofixedarea?customerIdStr="+customerIdStr+"&fixedAreaId="+model.getId()).put(null);

        return SUCCESS;
    }
    //属性驱动
    private Integer courierId;
    private Integer takeTimeId;

    public void setCourierId(Integer courierId) {
        this.courierId = courierId;
    }

    public void setTakeTimeId(Integer takeTimeId) {
        this.takeTimeId = takeTimeId;
    }

    //快递员排班功能
    @Action(value = "fixedArea_associationCourierToFixedArea",results = {@Result(name = "success", type = "redirect",
            location = "/pages/base/fixed_area.html")})
    public String associationCourierToFixedArea(){
        fixedAreaService.associationCourierToFixedArea(courierId,takeTimeId,model);
        return SUCCESS;
    }

}