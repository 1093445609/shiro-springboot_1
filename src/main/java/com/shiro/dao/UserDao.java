package com.shiro.dao;

import com.shiro.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@Mapper
public interface UserDao {

    //根据用户名查密码 和盐 id
    @Select ("SELECT * FROM USER WHERE NAME =#{name}")
    User getPwdByName(String name);

    //插入一个用户
    Integer  insert(User user);



    //三表查询,根据用户名查询角色
    List<String> listRoles(String name);

    //根据用户名查询 权限 五表查询
    List<String>  listPermissions(String name);

}
