package com.shiro;

import com.shiro.dao.RoleDao;
import com.shiro.pojo.User;
import com.shiro.service.RoleService;
import com.shiro.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class ShiroApplicationTests {
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;

    @Test
    void contextLoads() {
        User user = new User ( );
        user.setName ("wang5");
        user.setPassword ("123456");
        Integer result = userService.insert (user);
        if (result.equals ("1")) {
            System.out.println ("插入成功");
        } else {
            System.out.println ("插入失败,可能是用户名重复:" + result);
        }
    }

    @Test
    void  test(){
        User wang5 = userService.getPwdByName ("wang5");
        if (null==wang5) return;
        Integer[] rids={2,3};
        Integer integer = roleService.insert (rids, (int) wang5.getId ( ));
        System.out.println(integer);

    }



}
