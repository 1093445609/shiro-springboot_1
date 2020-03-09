package com.shiro.config.realmconfig;


import com.shiro.pojo.User;
import com.shiro.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;

/**授权和认证逻**/
public class CustomRealm extends AuthorizingRealm {
    @Autowired
    UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println ("执行授权逻辑1");
        SimpleAuthorizationInfo simpleInfo = new SimpleAuthorizationInfo ( );
        //获取登录用户名
        String name = (String) principalCollection.getPrimaryPrincipal ( );
        //之后在数据库查询他是什么角色 什么权限一一添加他们
        System.out.println ("查询角色和权限:" + name);
        List<String> listRoles = userService.listRoles (name);
        // simpleInfo.addRoles (listRoles);直接添加多个角色或者forench添加
        for (String role : listRoles) {
            simpleInfo.addRole (role);
        }
        //添加角色下的权限 比如crud
        List<String> listPermissions = userService.listPermissions (name);
        System.out.println (listPermissions.size ( ));
      //  simpleInfo.addStringPermissions (listPermissions);
        //设置好权限返回
        return simpleInfo;
    }

       @Override
    protected SimpleAuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        ;
        //加这一步的目的是在Post请求的时候会先进认证，然后在到请求
        if (null == authenticationToken.getPrincipal ( )) {
            return null;
        }
        System.out.println ("执行认证逻辑2222222222222222" + getName ( ));

        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        //获取用户登陆的名
        String name = token.getUsername ( );
        //查出的密码和盐封装成User
        User user = userService.getPwdByName (name);

        if (null == user) {
            //用户名不存在
            return null;//抛出一个空的对象 抛出异常UnknowAccountException
        }
        //密码
        String pwd = user.getPassword ( );
        //盐
        String salt = user.getSalt ( );
        //真盐=name+salt
        salt = name + salt+"a";

        //这里验证authenticationToken和simpleAuthenticationInfo的信息
        return new SimpleAuthenticationInfo (name, pwd, ByteSource.Util.bytes (salt), getName ( ));
    }
}
