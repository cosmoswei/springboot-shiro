package com.wei.config;

import com.wei.mapper.UserMapper;
import com.wei.pojo.User;
import com.wei.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.SubjectRunnable;
import org.springframework.beans.factory.annotation.Autowired;

//自定义的Realm
public class UserRealm extends AuthorizingRealm {

    @Autowired
    UserService userService;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("===执行了授权===");
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
//        simpleAuthorizationInfo.addStringPermission("user:add");

        //拿到当前登录的对象
        Subject subject = SecurityUtils.getSubject();
        //拿到对象
        User currentUser = (User) subject.getPrincipal();
        //设置当前对象的权限
        simpleAuthorizationInfo.addStringPermission(currentUser.getPerms());

        return simpleAuthorizationInfo;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("===执行了认证===");

//        String name = "root";
//        String password = "123456";

        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        //连接真实数据库
        User user = userService.queryUserByName(usernamePasswordToken.getUsername());
        if (user == null) {
            return null;
        }

        Subject currentSubject=SecurityUtils.getSubject();
        Session session = currentSubject.getSession();
        session.setAttribute("loginUser",user);

        //Shiro加密操作
        return new SimpleAuthenticationInfo(user, user.getPassword(), "");

//        if (userService.queryUserByName(usernamePasswordToken.getUsername()) == null) {
//            return null;
//        }
//        return new SimpleAuthenticationInfo("", userService.queryUserByName(usernamePasswordToken.getUsername()).getPassword(), "");


    }
}
