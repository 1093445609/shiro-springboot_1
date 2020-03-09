package com.shiro.service;

import org.apache.ibatis.annotations.Param;

public interface RoleService {

    /**
     * 一个用户绑定多个角色
     * @param roleIds  角色id数组
     * @param uid      用户id
     * @return
     */
    Integer insert(Integer[] roleIds, Integer uid);


}
