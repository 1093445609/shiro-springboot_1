package com.shiro.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface RoleDao {
    //创建用户时    关联角色和用户就可以了
    //因为角色和权限直接关联
    //一般创建一个用户不需要设置权限表
    Integer insert(@Param ("array") Integer[] array,@Param ("uid") Integer uid);


}
