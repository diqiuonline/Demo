package cn.itcast.bos.web.action.system;

import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.service.system.PermissionService;
import cn.itcast.bos.web.action.common.BaseAction;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * User: 李锦卓
 * Time: 2018/9/10 18:56
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class PermissionAction extends BaseAction<Permission> {
    @Autowired
    private PermissionService permissionService;
    //查询权限
    @Action(value = "permission_list", results = {@Result(name = "success", type = "json")})
    public String lgist() {
        List<Permission> list = permissionService.findAll();
        ServletActionContext.getContext().getValueStack().push(list);
        return SUCCESS;
    }
    //权限添加
    @Action(value = "permission_save", results = {@Result(name = "success", type = "redirect", location = "/pages/system/permission.html")})
    public String save() {
        permissionService.save(model);
        return SUCCESS;
    }

}