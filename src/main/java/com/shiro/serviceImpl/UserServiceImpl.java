package com.shiro.serviceImpl;

import com.shiro.dao.UserDao;
import com.shiro.pojo.User;
import com.shiro.service.UserService;
import com.shiro.util.PasswordGenerateUtil;
import org.apache.ibatis.annotations.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;

    @Override
    @Cacheable(cacheNames = "users",key ="#name",unless="#result == null")
    public User getPwdByName(String name) {

        System.out.println(name+":数据库查询用户中.....");

        return userDao.getPwdByName (name);
    }

    @Override
    @Cacheable(cacheNames = "roles",key = "#name")
    public List<String> listRoles(String name) {
        System.out.println(name+":数据库查询角色中.....");
        return userDao.listRoles (name);
    }

    @Override
    @Cacheable(cacheNames = "Permissions",key = "#name")
    public List<String> listPermissions(String name) {
        System.out.println(name+":数据库查询用户权限中");
        return userDao.listPermissions (name);
    }

    @Override
    public Integer insert(User user) {
        User name = userDao.getPwdByName (user.getName ( ));
        if (null!=name){
            //用户名重复了
            return -1;
        }
        //假盐 还没加工
        String salt = Long.toString(System.currentTimeMillis());
        //假盐,因为数据库的盐还不是真正的盐  解密的时候需要再次加工
        user.setSalt (salt);

        String password = user.getPassword ( );
        //MD5加密生成密文
        String password1 = PasswordGenerateUtil.getPassword (user.getName ( ), password, salt, 2);
       //加功后的密文
        user.setPassword (password1);

        Integer insert = userDao.insert (user);
        return insert;
    }
}
