package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;
import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: 李锦卓
 * Time: 2018/8/18 20:42
 */
@ParentPackage("struts-default")
@Namespace("/")
@Actions
@Controller
@Scope("prototype")
public class StandardAction extends ActionSupport implements ModelDriven<Standard> {
	//模型驱动
	private Standard standard = new Standard();
	//属性驱动
	private Integer page;
	private Integer rows;
	public void setPage(Integer page) {
		this.page = page;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	@Autowired
	private StandardService standardService;

	@Override
	public Standard getModel() {
		return standard;
	}
	//添加收派标准
	@Action(value = "standard_save",results = {@Result(name = "success",location = "/pages/base/standard.html")})
	public String save() {
		System.out.println("添加收派标准");
		standardService.save(standard);
		return SUCCESS;
	}
	//分页显示
	@Action(value = "standard_pageQuery")
	public void pageQuery() throws IOException {
		ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
		//调用业务层
		Pageable pageable = new PageRequest(page-1,rows);
		Page<Standard>pageDate = standardService.findPageDate(pageable);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", pageDate.getTotalElements());
		result.put("rows", pageDate.getContent());
		//将map转换为json数据返回
		String string = JSONObject.toJSONString(result);
		ServletActionContext.getResponse().getWriter().println(string);
	}
	//取派标准显示
	@Action(value = "standard_findAll")
	public void findAll() throws IOException {
		ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
		List<Standard>standards = standardService.findAll();
		String string = JSONObject.toJSONString(standards);
		ServletActionContext.getResponse().getWriter().println(string);
	}
}