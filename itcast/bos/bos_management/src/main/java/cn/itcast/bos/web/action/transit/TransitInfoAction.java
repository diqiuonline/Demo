package cn.itcast.bos.web.action.transit;

import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.service.transit.TransitInfoService;
import cn.itcast.bos.web.action.common.BaseAction;
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
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * User: 李锦卓
 * Time: 2018/9/12 19:37
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class TransitInfoAction extends BaseAction<TransitInfo> {
    @Autowired
    private TransitInfoService transitInfoService;
    private String wayBillIds;
    public void setWayBillIds(String wayBillIds) {
        this.wayBillIds = wayBillIds;
    }
    @Action(value = "transit_create", results = {@Result(name = "success", type = "json")})
    //开启中转配送
    public String create() {
        //掉用业务层 保存transitInfo
        Map<String,Object>result = new HashMap<String, Object>();
        try {
            transitInfoService.createTransit(wayBillIds);
            result.put("success", true);
            result.put("msg","开启中转配送成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("msg","开启中转配送失败 运单已经出发");
        }
        ServletActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }

    //查询中转配送
    @Action(value = "transit_pageQuery", results = {@Result(name = "success", type = "json")})
    public String pageQuery() throws IOException {
        //分页查询
        Pageable pageable = new PageRequest(page - 1, rows);
        Page<TransitInfo>pageData = transitInfoService.findPage(pageable);
        PushPageData(pageData);
        return SUCCESS;
    }


}