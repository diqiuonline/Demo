package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;
import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.commons.lang3.StringUtils;
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

import javax.persistence.criteria.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: 李锦卓
 * Time: 2018/8/19 21:23
 */
@ParentPackage("struts-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class CourierAction extends ActionSupport implements ModelDriven<Courier> {
	//模型驱动
	private Courier courier = new Courier();
	@Override
	public Courier getModel() {
		return courier;
	}

	//注入service
	@Autowired
	private CourierService courierService;

	//添加快递员
	@Action(value = "courier_save",results = {@Result(name = "success",location = "/pages/base/courier.html")})
	public String save() {
		courierService.save(courier);
		return SUCCESS;
	}
	//属性驱动
	private Integer page;
	private Integer rows;

	public void setPage(Integer page) {
		this.page = page;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	//快递员查询
	@Action(value = "courier_pageQuery")
	public void pageQuery() throws IOException {
		Specification<Courier>specification = new Specification<Courier>() {
			@Override
			/**
			 * 构造条件查询 如果方法返回noll 代表无条件查询
			 * Root参数 获取条件表达式 name = ？ ，age = ？
			 * criteriaQuery 参数 构造简单条件查询返回 提供where方法
			 * criteriaBuilder 参数 构造predicate 对象 条件对象 构造复杂查询效果
			 */
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				//当前root根对象courier
				List<Predicate>list = new ArrayList<Predicate>();
				//单表查询
				if (StringUtils.isNoneBlank(courier.getCourierNum())) {
					//进行快递员工号查询
					//courierNum=?
					Predicate p1 = cb.equal(root.get("courierNum").as(String.class), courier.getCourierNum());
					list.add(p1);
				}
				if (StringUtils.isNoneBlank(courier.getCompany())){
					//进行公司查询
					//company like %?%
					Predicate p2 = cb.like(root.get("company").as(String.class), "%" + courier.getCompany() + "%");
					list.add(p2);
				}
				if (StringUtils.isNoneBlank(courier.getType())){
					//进行快递员类型查询 等值查询
					//type = ?
					Predicate p3 = cb.equal(root.get("type").as(String.class), courier.getType());
					list.add(p3);
				}
				//多表查询（查询当前对象 关联对象 对应数据表）
				//使用courier（Root）关联Standard
				Join<Object, Object> standardroot = root.join("standard", JoinType.INNER);
				if (courier.getStandard()!=null && StringUtils.isNoneBlank(courier.getStandard().getName())){
					//进行收派标准模糊查询
					//standard.name like %?%
					Predicate p4 = cb.like(standardroot.get("name").as(String.class), "%" + courier.getStandard().getName() + "%");
					list.add(p4);
				}
				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
		Pageable pageable = new PageRequest(page-1,rows);

		Page<Courier>couriers = courierService.findPageData(specification,pageable);
		Map<String,Object>result = new HashMap<String, Object>();
		result.put("total", couriers.getTotalElements());
		result.put("rows",couriers.getContent());
		String string = JSONObject.toJSONString(result);
		ServletActionContext.getResponse().getWriter().println(string);
	}
	//属性驱动
    private String ids;

    public void setIds(String ids) {
        this.ids = ids;
    }
    //作废
    @Action(value = "courier_delBeath",results = {@Result(name = "success" ,location = "/pages/base/courier.html")})
    public String delBeath() {
        String[] idArrer = ids.split(",");
        courierService.delBeath(idArrer);
        return SUCCESS;
    }
    //还原
    @Action(value = "courier_redBeath", results = {@Result(name = "success", location = "/pages/base/courier.html")})
    public String redBeath() {
        String[] idArrer = ids.split(",");
        courierService.redBeath(idArrer);
        return SUCCESS;
    }

    //查询未关联定区快递员
    @Action(value = "courier_findnoassociation")
    public void findNoAssociation() throws IOException {
        ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
    	List<Courier>list = courierService.findNoAssociation();
        String string = JSONObject.toJSONString(list);
        ServletActionContext.getResponse().getWriter().println(string);

    }
}