package cn.itcast.bos.realm;

import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: 李锦卓
 * Time: 2018/9/9 19:17
 */
public class BosRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("授权管理");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //根据当前登陆用户查询对应校色和权限
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        //循环遍历用户获取校色
        for (Role role : user.getRoles()) {
            authorizationInfo.addRole(role.getKeyword());
            for (Permission permission : role.getPermissions()) {
                authorizationInfo.addStringPermission(permission.getKeyword());
            }
        }
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("shiro 认证管理");
        //转换token
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        //根据用户名查询用户信息
        User user = userService.findByUsername(usernamePasswordToken.getUsername());
        if (user == null) {
            //用户名不存在
            //参数一 期望登陆后 保存在subject中的信息
            //参数二 如果返回null 报用户名
            //参数三 realmi名称
            return null;
        } else {
            //用户名存在
            // 当返回用户密码时 securityManager 安全管理器 自动比较返回密码和用户输入的密码是否一致
            //如果密码一致 登陆成功 如果密码不一致 保密吗错误异常
            String ii = getName();
            System.out.println(ii);
            return new SimpleAuthenticationInfo(user,user.getPassword(),getName());
        }
    }
}