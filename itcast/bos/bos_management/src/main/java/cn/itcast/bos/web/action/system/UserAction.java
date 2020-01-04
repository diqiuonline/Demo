package cn.itcast.bos.web.action.system;

import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.UserService;
import cn.itcast.bos.web.action.common.BaseAction;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
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
 * Time: 2018/9/9 18:57
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class UserAction extends BaseAction<User> {
    @Autowired
    private UserService userService;


    @Action(value = "user_login", results = {
            @Result(name = "success", type = "redirect", location = "/index.html"),
            @Result(name = "login", type = "redirect", location = "/login.html")})
    public String login() {
        //用户名和密码都保存在modul中
        //基于shiro实现
        Subject subject = SecurityUtils.getSubject();
        //用户名和密码
        AuthenticationToken token = new UsernamePasswordToken(model.getUsername(), model.getPassword());
        try {
            subject.login(token);
            //登陆成功
            //将用户信息保存到session中
            //ServletActionContext.getRequest().getSession().setAttribute("user", model);
            return SUCCESS;
        } catch (Exception e) {
            //登陆失败
            e.printStackTrace();
            return LOGIN;
        }
    }
    //用户退出
    @Action(value = "user_logout", results = {@Result(name = "success", type = "redirect", location = "/index.html")})
    public String logout() {
        //基于shiro完成退出
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return SUCCESS;
    }

    //用户查询
    @Action(value = "user_list", results = {@Result(name = "success", type = "json")})
    public String list() {
        List<User> list = userService.findAll();
        ServletActionContext.getContext().getValueStack().push(list);
        return SUCCESS;
    }

    //属性驱动
    private String[] roleIds;

    public void setRoleIds(String[] roleIds) {
        this.roleIds = roleIds;
    }

    //用户添加
    @Action(value = "user_save", results = {@Result(name = "success", type = "redirect", location = "/pages/system/userlist.html")})
    public String save() {
        userService.save(model,roleIds);
        return SUCCESS;
    }
}