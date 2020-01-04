package cn.itcast.bos.web.action.system;

import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.MenuService;
import cn.itcast.bos.web.action.common.BaseAction;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * User: 李锦卓
 * Time: 2018/9/9 21:42
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("ptototype")
public class MenuAction extends BaseAction<Menu> {
    @Autowired
    private MenuService menuService;
    //查询菜单列表
    @Action(value = "menu_list",results = {@Result(name = "success",type = "json")})
    public String list() throws IOException {
        List<Menu> list = menuService.findAll();
        ServletActionContext.getContext().getValueStack().push(list);
        return SUCCESS;
    }
    //保存菜单
    @Action(value = "menu_save", results = {@Result(name = "success", type = "redirect",location = "/pages/system/menu.html")})
    public String save() {
        menuService.save(model);
        return SUCCESS;
    }

    //查询指定菜单
    @Action(value = "menu_showmenu", results = {@Result(name = "success", type = "json")})
    public String showmenu() {
        //调用业务层 查询当前用户具有菜单 列表
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        List<Menu>list = menuService.findByUser(user);
        ServletActionContext.getContext().getValueStack().push(list);

        return SUCCESS;
    }
}